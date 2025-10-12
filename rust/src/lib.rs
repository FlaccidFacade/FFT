use std::f64::consts::PI;

#[derive(Debug, Clone, Copy)]
pub struct Complex {
    pub real: f64,
    pub imag: f64,
}

impl Complex {
    pub fn new(real: f64, imag: f64) -> Self {
        Complex { real, imag }
    }
    
    pub fn add(&self, other: &Complex) -> Complex {
        Complex {
            real: self.real + other.real,
            imag: self.imag + other.imag,
        }
    }
    
    pub fn subtract(&self, other: &Complex) -> Complex {
        Complex {
            real: self.real - other.real,
            imag: self.imag - other.imag,
        }
    }
    
    pub fn multiply(&self, other: &Complex) -> Complex {
        Complex {
            real: self.real * other.real - self.imag * other.imag,
            imag: self.real * other.imag + self.imag * other.real,
        }
    }
    
    pub fn abs(&self) -> f64 {
        (self.real * self.real + self.imag * self.imag).sqrt()
    }
}

/// Compute the Fast Fourier Transform of the input signal.
/// Uses the Cooley-Tukey FFT algorithm.
pub fn fft(x: &[Complex]) -> Vec<Complex> {
    let mut n = x.len();
    
    // Base case
    if n <= 1 {
        return x.to_vec();
    }
    
    // Copy input and pad to next power of 2 if necessary
    let mut input = x.to_vec();
    if (n & (n - 1)) != 0 {
        let mut next_pow2 = 1;
        while next_pow2 < n {
            next_pow2 <<= 1;
        }
        input.resize(next_pow2, Complex::new(0.0, 0.0));
        n = next_pow2;
    }
    
    // Divide
    let even: Vec<Complex> = input.iter().step_by(2).copied().collect();
    let odd: Vec<Complex> = input.iter().skip(1).step_by(2).copied().collect();
    
    // Conquer
    let even_fft = fft(&even);
    let odd_fft = fft(&odd);
    
    // Combine
    let mut result = vec![Complex::new(0.0, 0.0); n];
    for k in 0..n / 2 {
        let angle = -2.0 * PI * (k as f64) / (n as f64);
        let t = Complex::new(angle.cos(), angle.sin()).multiply(&odd_fft[k]);
        result[k] = even_fft[k].add(&t);
        result[k + n / 2] = even_fft[k].subtract(&t);
    }
    
    result
}

#[cfg(test)]
mod tests {
    use super::*;
    
    #[test]
    fn test_correctness() {
        // Test case 1: Simple signal
        let signal: Vec<Complex> = (0..8)
            .map(|i| Complex::new(if i < 4 { 1.0 } else { 0.0 }, 0.0))
            .collect();
        
        let result = fft(&signal);
        
        // DC component should be 4
        assert!((result[0].real - 4.0).abs() < 1e-10);
        
        // Test case 2: Impulse
        let impulse: Vec<Complex> = (0..8)
            .map(|i| Complex::new(if i == 0 { 1.0 } else { 0.0 }, 0.0))
            .collect();
        
        let result = fft(&impulse);
        
        // All bins should be 1
        for val in &result {
            assert!((val.real - 1.0).abs() < 1e-10);
            assert!(val.imag.abs() < 1e-10);
        }
        
        // Test case 3: Constant signal
        let constant: Vec<Complex> = vec![Complex::new(1.0, 0.0); 8];
        let result = fft(&constant);
        
        // Only DC component should be non-zero
        assert!((result[0].real - 8.0).abs() < 1e-10);
        for i in 1..result.len() {
            assert!(result[i].abs() < 1e-10);
        }
    }
    
    #[test]
    fn test_edge_cases() {
        // Empty input
        let empty: Vec<Complex> = vec![];
        let result = fft(&empty);
        assert_eq!(result.len(), 0);
        
        // Single element
        let single = vec![Complex::new(5.0, 0.0)];
        let result = fft(&single);
        assert_eq!(result.len(), 1);
        assert_eq!(result[0].real, 5.0);
        
        // Power of 2 length
        let power_of_2 = vec![
            Complex::new(1.0, 0.0),
            Complex::new(0.0, 0.0),
            Complex::new(1.0, 0.0),
            Complex::new(0.0, 0.0),
        ];
        let result = fft(&power_of_2);
        assert_eq!(result.len(), 4);
        
        // Non-power of 2
        let non_power = vec![
            Complex::new(1.0, 0.0),
            Complex::new(2.0, 0.0),
            Complex::new(3.0, 0.0),
        ];
        let result = fft(&non_power);
        assert_eq!(result.len(), 4);
    }
}
