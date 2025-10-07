use fft::{Complex, fft};
use std::time::Instant;

fn main() {
    // Example usage
    let signal = vec![1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0];
    let input: Vec<Complex> = signal.iter().map(|&x| Complex::new(x, 0.0)).collect();
    
    let result = fft(&input);
    
    println!("Input signal: {:?}", signal);
    println!("FFT result:");
    for (i, val) in result.iter().enumerate() {
        println!("  {}: {:.4} + {:.4}i", i, val.real, val.imag);
    }
    
    // Run tests
    println!("\n==================================================");
    println!("Rust FFT Test Suite");
    println!("==================================================");
    
    test_correctness();
    test_edge_cases();
    test_performance();
    
    println!("==================================================");
    println!("ALL TESTS PASSED");
    println!("==================================================");
}

fn test_correctness() {
    println!("\n=== Test 1: Correctness ===");
    
    // Test case 1: Simple signal
    let signal: Vec<Complex> = (0..8)
        .map(|i| Complex::new(if i < 4 { 1.0 } else { 0.0 }, 0.0))
        .collect();
    
    let result = fft(&signal);
    
    // DC component should be 4
    let dc = result[0].real;
    assert!((dc - 4.0).abs() < 1e-10, "DC component should be 4, got {}", dc);
    println!("✓ DC component test passed: {:.4}", dc);
    
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
    println!("✓ Impulse test passed");
    
    // Test case 3: Constant signal
    let constant: Vec<Complex> = vec![Complex::new(1.0, 0.0); 8];
    let result = fft(&constant);
    
    // Only DC component should be non-zero
    assert!((result[0].real - 8.0).abs() < 1e-10, "DC should be 8");
    for i in 1..result.len() {
        assert!(result[i].abs() < 1e-10, "Bin {} should be zero", i);
    }
    println!("✓ Constant signal test passed");
    println!("Test 1: PASSED\n");
}

fn test_edge_cases() {
    println!("=== Test 2: Edge Cases ===");
    
    // Empty input
    let empty: Vec<Complex> = vec![];
    let result = fft(&empty);
    assert_eq!(result.len(), 0, "Empty input should return empty output");
    println!("✓ Empty input test passed");
    
    // Single element
    let single = vec![Complex::new(5.0, 0.0)];
    let result = fft(&single);
    assert_eq!(result.len(), 1);
    assert_eq!(result[0].real, 5.0);
    println!("✓ Single element test passed");
    
    // Power of 2 length
    let power_of_2 = vec![
        Complex::new(1.0, 0.0),
        Complex::new(0.0, 0.0),
        Complex::new(1.0, 0.0),
        Complex::new(0.0, 0.0),
    ];
    let result = fft(&power_of_2);
    assert_eq!(result.len(), 4, "Length should be preserved for power of 2");
    println!("✓ Power of 2 test passed");
    
    // Non-power of 2
    let non_power = vec![
        Complex::new(1.0, 0.0),
        Complex::new(2.0, 0.0),
        Complex::new(3.0, 0.0),
    ];
    let result = fft(&non_power);
    assert_eq!(result.len(), 4, "Non-power of 2 should be padded to next power");
    println!("✓ Non-power of 2 padding test passed");
    
    println!("Test 2: PASSED\n");
}

fn test_performance() {
    println!("=== Test 3: Performance Benchmark ===");
    
    let sizes = [64, 256, 1024, 4096];
    
    for &size in &sizes {
        let signal: Vec<Complex> = (0..size)
            .map(|i| Complex::new((i % 2) as f64, 0.0))
            .collect();
        
        let start = Instant::now();
        let _result = fft(&signal);
        let elapsed = start.elapsed();
        
        println!("Size {:5}: {:8.4} ms", size, elapsed.as_secs_f64() * 1000.0);
    }
    
    // Performance test
    let signal: Vec<Complex> = (0..4096)
        .map(|i| Complex::new((i % 2) as f64, 0.0))
        .collect();
    
    let start = Instant::now();
    let _result = fft(&signal);
    let elapsed = start.elapsed();
    
    assert!(
        elapsed.as_secs_f64() < 5.0,
        "FFT of 4096 samples took too long: {:.4}s",
        elapsed.as_secs_f64()
    );
    
    println!("\n✓ Performance test passed (4096 samples: {:.4} ms)", elapsed.as_secs_f64() * 1000.0);
    println!("Test 3: PASSED\n");
}
