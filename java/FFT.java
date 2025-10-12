/**
 * FFT implementation using Java standard library.
 * Implements the Cooley-Tukey FFT algorithm.
 */
public class FFT {
    
    /**
     * Complex number class for FFT calculations.
     */
    public static class Complex {
        public final double real;
        public final double imag;
        
        public Complex(double real, double imag) {
            this.real = real;
            this.imag = imag;
        }
        
        public Complex add(Complex other) {
            return new Complex(this.real + other.real, this.imag + other.imag);
        }
        
        public Complex subtract(Complex other) {
            return new Complex(this.real - other.real, this.imag - other.imag);
        }
        
        public Complex multiply(Complex other) {
            double newReal = this.real * other.real - this.imag * other.imag;
            double newImag = this.real * other.imag + this.imag * other.real;
            return new Complex(newReal, newImag);
        }
        
        public double abs() {
            return Math.sqrt(real * real + imag * imag);
        }
        
        @Override
        public String toString() {
            return String.format("%.4f + %.4fi", real, imag);
        }
    }
    
    /**
     * Compute the Fast Fourier Transform of the input signal.
     * 
     * @param x Input signal as an array of complex numbers
     * @return FFT of the input signal
     */
    public static Complex[] fft(Complex[] x) {
        int n = x.length;
        
        // Base case
        if (n <= 1) {
            return x;
        }
        
        // Pad to next power of 2 if necessary
        if ((n & (n - 1)) != 0) {
            int nextPow2 = 1;
            while (nextPow2 < n) {
                nextPow2 <<= 1;
            }
            Complex[] padded = new Complex[nextPow2];
            System.arraycopy(x, 0, padded, 0, n);
            for (int i = n; i < nextPow2; i++) {
                padded[i] = new Complex(0, 0);
            }
            x = padded;
            n = nextPow2;
        }
        
        // Divide
        Complex[] even = new Complex[n / 2];
        Complex[] odd = new Complex[n / 2];
        for (int i = 0; i < n / 2; i++) {
            even[i] = x[2 * i];
            odd[i] = x[2 * i + 1];
        }
        
        // Conquer
        even = fft(even);
        odd = fft(odd);
        
        // Combine
        Complex[] result = new Complex[n];
        for (int k = 0; k < n / 2; k++) {
            double angle = -2 * Math.PI * k / n;
            Complex t = new Complex(Math.cos(angle), Math.sin(angle)).multiply(odd[k]);
            result[k] = even[k].add(t);
            result[k + n / 2] = even[k].subtract(t);
        }
        
        return result;
    }
    
    public static void main(String[] args) {
        // Example usage
        double[] signal = {1, 1, 1, 1, 0, 0, 0, 0};
        Complex[] input = new Complex[signal.length];
        for (int i = 0; i < signal.length; i++) {
            input[i] = new Complex(signal[i], 0);
        }
        
        Complex[] result = fft(input);
        
        System.out.print("Input signal: ");
        for (double val : signal) {
            System.out.print(val + " ");
        }
        System.out.println();
        
        System.out.println("FFT result:");
        for (int i = 0; i < result.length; i++) {
            System.out.println("  " + i + ": " + result[i]);
        }
    }
}
