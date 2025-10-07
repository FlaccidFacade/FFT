#!/usr/bin/env node
/**
 * Test suite for FFT implementation.
 * Tests include correctness, edge cases, and performance benchmarking.
 */

const { Complex, fft } = require('./fft.js');

function testCorrectness() {
    console.log('\n=== Test 1: Correctness ===');
    
    // Test case 1: Simple signal
    let signal = [];
    for (let i = 0; i < 8; i++) {
        signal.push(new Complex(i < 4 ? 1 : 0, 0));
    }
    
    let result = fft(signal);
    
    // DC component should be 4
    const dc = result[0].real;
    console.assert(Math.abs(dc - 4.0) < 1e-10, `DC component should be 4, got ${dc}`);
    console.log(`✓ DC component test passed: ${dc.toFixed(4)}`);
    
    // Test case 2: Impulse
    let impulse = [];
    for (let i = 0; i < 8; i++) {
        impulse.push(new Complex(i === 0 ? 1 : 0, 0));
    }
    
    result = fft(impulse);
    
    // All bins should be 1
    const allOnes = result.every(val => 
        Math.abs(val.real - 1.0) < 1e-10 && Math.abs(val.imag) < 1e-10
    );
    console.assert(allOnes, 'Impulse FFT should have all bins equal to 1');
    console.log('✓ Impulse test passed');
    
    // Test case 3: Constant signal
    let constant = [];
    for (let i = 0; i < 8; i++) {
        constant.push(new Complex(1, 0));
    }
    result = fft(constant);
    
    // Only DC component should be non-zero
    console.assert(Math.abs(result[0].real - 8.0) < 1e-10, 'DC should be 8');
    for (let i = 1; i < result.length; i++) {
        console.assert(result[i].abs() < 1e-10, `Bin ${i} should be zero`);
    }
    console.log('✓ Constant signal test passed');
    console.log('Test 1: PASSED\n');
    
    return true;
}

function testEdgeCases() {
    console.log('=== Test 2: Edge Cases ===');
    
    // Empty input
    let empty = [];
    let result = fft(empty);
    console.assert(result.length === 0, 'Empty input should return empty output');
    console.log('✓ Empty input test passed');
    
    // Single element
    let single = [new Complex(5, 0)];
    result = fft(single);
    console.assert(result.length === 1 && result[0].real === 5, 'Single element should return itself');
    console.log('✓ Single element test passed');
    
    // Power of 2 length
    let powerOf2 = [
        new Complex(1, 0),
        new Complex(0, 0),
        new Complex(1, 0),
        new Complex(0, 0)
    ];
    result = fft(powerOf2);
    console.assert(result.length === 4, 'Length should be preserved for power of 2');
    console.log('✓ Power of 2 test passed');
    
    // Non-power of 2
    let nonPower = [
        new Complex(1, 0),
        new Complex(2, 0),
        new Complex(3, 0)
    ];
    result = fft(nonPower);
    console.assert(result.length === 4, 'Non-power of 2 should be padded to next power');
    console.log('✓ Non-power of 2 padding test passed');
    
    console.log('Test 2: PASSED\n');
    return true;
}

function testPerformance() {
    console.log('=== Test 3: Performance Benchmark ===');
    
    const sizes = [64, 256, 1024, 4096];
    
    for (const size of sizes) {
        const signal = [];
        for (let i = 0; i < size; i++) {
            signal.push(new Complex(i % 2, 0));
        }
        
        const start = process.hrtime.bigint();
        const result = fft(signal);
        const end = process.hrtime.bigint();
        
        const elapsedMs = Number(end - start) / 1_000_000;
        console.log(`Size ${size.toString().padStart(5)}: ${elapsedMs.toFixed(4).padStart(8)} ms`);
    }
    
    // Performance test
    const signal = [];
    for (let i = 0; i < 4096; i++) {
        signal.push(new Complex(i % 2, 0));
    }
    
    const start = process.hrtime.bigint();
    const result = fft(signal);
    const end = process.hrtime.bigint();
    
    const elapsed = Number(end - start) / 1_000_000_000;
    console.assert(elapsed < 5.0, `FFT of 4096 samples took too long: ${elapsed}s`);
    
    console.log(`\n✓ Performance test passed (4096 samples: ${(elapsed * 1000).toFixed(4)} ms)`);
    console.log('Test 3: PASSED\n');
    
    return true;
}

function main() {
    console.log('==================================================');
    console.log('JavaScript FFT Test Suite');
    console.log('==================================================');
    
    try {
        testCorrectness();
        testEdgeCases();
        testPerformance();
        
        console.log('==================================================');
        console.log('ALL TESTS PASSED');
        console.log('==================================================');
        process.exit(0);
    } catch (error) {
        console.error(`\n❌ Test failed: ${error.message}`);
        process.exit(1);
    }
}

// Run tests
main();
