/**
 * Test suite for FFT implementation.
 * Tests include correctness, edge cases, and performance benchmarking.
 */
public class TestFFT {
    
    private static boolean testCorrectness() {
        System.out.println("\n=== Test 1: Correctness ===");
        
        // Test case 1: Simple signal
        FFT.Complex[] signal = new FFT.Complex[8];
        for (int i = 0; i < 8; i++) {
            signal[i] = new FFT.Complex(i < 4 ? 1 : 0, 0);
        }
        
        FFT.Complex[] result = FFT.fft(signal);
        
        // DC component should be 4
        double dc = result[0].real;
        assert Math.abs(dc - 4.0) < 1e-10 : "DC component should be 4, got " + dc;
        System.out.printf("✓ DC component test passed: %.4f\n", dc);
        
        // Test case 2: Impulse
        FFT.Complex[] impulse = new FFT.Complex[8];
        for (int i = 0; i < 8; i++) {
            impulse[i] = new FFT.Complex(i == 0 ? 1 : 0, 0);
        }
        
        result = FFT.fft(impulse);
        
        // All bins should be 1
        boolean allOnes = true;
        for (FFT.Complex val : result) {
            if (Math.abs(val.real - 1.0) >= 1e-10 || Math.abs(val.imag) >= 1e-10) {
                allOnes = false;
                break;
            }
        }
        assert allOnes : "Impulse FFT should have all bins equal to 1";
        System.out.println("✓ Impulse test passed");
        
        // Test case 3: Constant signal
        FFT.Complex[] constant = new FFT.Complex[8];
        for (int i = 0; i < 8; i++) {
            constant[i] = new FFT.Complex(1, 0);
        }
        result = FFT.fft(constant);
        
        // Only DC component should be non-zero
        assert Math.abs(result[0].real - 8.0) < 1e-10 : "DC should be 8";
        for (int i = 1; i < result.length; i++) {
            assert result[i].abs() < 1e-10 : "Bin " + i + " should be zero";
        }
        System.out.println("✓ Constant signal test passed");
        System.out.println("Test 1: PASSED\n");
        
        return true;
    }
    
    private static boolean testEdgeCases() {
        System.out.println("=== Test 2: Edge Cases ===");
        
        // Empty input
        FFT.Complex[] empty = new FFT.Complex[0];
        FFT.Complex[] result = FFT.fft(empty);
        assert result.length == 0 : "Empty input should return empty output";
        System.out.println("✓ Empty input test passed");
        
        // Single element
        FFT.Complex[] single = {new FFT.Complex(5, 0)};
        result = FFT.fft(single);
        assert result.length == 1 && result[0].real == 5 : "Single element should return itself";
        System.out.println("✓ Single element test passed");
        
        // Power of 2 length
        FFT.Complex[] powerOf2 = {
            new FFT.Complex(1, 0),
            new FFT.Complex(0, 0),
            new FFT.Complex(1, 0),
            new FFT.Complex(0, 0)
        };
        result = FFT.fft(powerOf2);
        assert result.length == 4 : "Length should be preserved for power of 2";
        System.out.println("✓ Power of 2 test passed");
        
        // Non-power of 2
        FFT.Complex[] nonPower = {
            new FFT.Complex(1, 0),
            new FFT.Complex(2, 0),
            new FFT.Complex(3, 0)
        };
        result = FFT.fft(nonPower);
        assert result.length == 4 : "Non-power of 2 should be padded to next power";
        System.out.println("✓ Non-power of 2 padding test passed");
        
        System.out.println("Test 2: PASSED\n");
        return true;
    }
    
    private static boolean testPerformance() {
        System.out.println("=== Test 3: Performance Benchmark ===");
        
        int[] sizes = {64, 256, 1024, 4096};
        
        for (int size : sizes) {
            FFT.Complex[] signal = new FFT.Complex[size];
            for (int i = 0; i < size; i++) {
                signal[i] = new FFT.Complex(i % 2, 0);
            }
            
            long start = System.nanoTime();
            FFT.Complex[] result = FFT.fft(signal);
            long end = System.nanoTime();
            
            double elapsedMs = (end - start) / 1_000_000.0;
            System.out.printf("Size %5d: %8.4f ms\n", size, elapsedMs);
        }
        
        // Performance test
        FFT.Complex[] signal = new FFT.Complex[4096];
        for (int i = 0; i < 4096; i++) {
            signal[i] = new FFT.Complex(i % 2, 0);
        }
        
        long start = System.nanoTime();
        FFT.Complex[] result = FFT.fft(signal);
        long end = System.nanoTime();
        
        double elapsed = (end - start) / 1_000_000_000.0;
        assert elapsed < 5.0 : "FFT of 4096 samples took too long: " + elapsed + "s";
        
        System.out.printf("\n✓ Performance test passed (4096 samples: %.4f ms)\n", elapsed * 1000);
        System.out.println("Test 3: PASSED\n");
        
        return true;
    }
    
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("Java FFT Test Suite");
        System.out.println("==================================================");
        
        try {
            testCorrectness();
            testEdgeCases();
            testPerformance();
            
            System.out.println("==================================================");
            System.out.println("ALL TESTS PASSED");
            System.out.println("==================================================");
            System.exit(0);
        } catch (AssertionError e) {
            System.err.println("\n❌ Test failed: " + e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.err.println("\n❌ Unexpected error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
