"use strict";
/**
 * Test suite for FFT implementation.
 * Tests include correctness, edge cases, and performance benchmarking.
 */
Object.defineProperty(exports, "__esModule", { value: true });
const fft_1 = require("./fft");
function testCorrectness() {
    console.log('\n=== Test 1: Correctness ===');
    // Test case 1: Simple signal
    let signal = [];
    for (let i = 0; i < 8; i++) {
        signal.push(new fft_1.Complex(i < 4 ? 1 : 0, 0));
    }
    let result = (0, fft_1.fft)(signal);
    // DC component should be 4
    const dc = result[0].real;
    if (Math.abs(dc - 4.0) >= 1e-10) {
        throw new Error(`DC component should be 4, got ${dc}`);
    }
    console.log(`✓ DC component test passed: ${dc.toFixed(4)}`);
    // Test case 2: Impulse
    let impulse = [];
    for (let i = 0; i < 8; i++) {
        impulse.push(new fft_1.Complex(i === 0 ? 1 : 0, 0));
    }
    result = (0, fft_1.fft)(impulse);
    // All bins should be 1
    const allOnes = result.every(val => Math.abs(val.real - 1.0) < 1e-10 && Math.abs(val.imag) < 1e-10);
    if (!allOnes) {
        throw new Error('Impulse FFT should have all bins equal to 1');
    }
    console.log('✓ Impulse test passed');
    // Test case 3: Constant signal
    let constant = [];
    for (let i = 0; i < 8; i++) {
        constant.push(new fft_1.Complex(1, 0));
    }
    result = (0, fft_1.fft)(constant);
    // Only DC component should be non-zero
    if (Math.abs(result[0].real - 8.0) >= 1e-10) {
        throw new Error('DC should be 8');
    }
    for (let i = 1; i < result.length; i++) {
        if (result[i].abs() >= 1e-10) {
            throw new Error(`Bin ${i} should be zero`);
        }
    }
    console.log('✓ Constant signal test passed');
    console.log('Test 1: PASSED\n');
    return true;
}
function testEdgeCases() {
    console.log('=== Test 2: Edge Cases ===');
    // Empty input
    let empty = [];
    let result = (0, fft_1.fft)(empty);
    if (result.length !== 0) {
        throw new Error('Empty input should return empty output');
    }
    console.log('✓ Empty input test passed');
    // Single element
    let single = [new fft_1.Complex(5, 0)];
    result = (0, fft_1.fft)(single);
    if (result.length !== 1 || result[0].real !== 5) {
        throw new Error('Single element should return itself');
    }
    console.log('✓ Single element test passed');
    // Power of 2 length
    let powerOf2 = [
        new fft_1.Complex(1, 0),
        new fft_1.Complex(0, 0),
        new fft_1.Complex(1, 0),
        new fft_1.Complex(0, 0)
    ];
    result = (0, fft_1.fft)(powerOf2);
    if (result.length !== 4) {
        throw new Error('Length should be preserved for power of 2');
    }
    console.log('✓ Power of 2 test passed');
    // Non-power of 2
    let nonPower = [
        new fft_1.Complex(1, 0),
        new fft_1.Complex(2, 0),
        new fft_1.Complex(3, 0)
    ];
    result = (0, fft_1.fft)(nonPower);
    if (result.length !== 4) {
        throw new Error('Non-power of 2 should be padded to next power');
    }
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
            signal.push(new fft_1.Complex(i % 2, 0));
        }
        const start = process.hrtime.bigint();
        const result = (0, fft_1.fft)(signal);
        const end = process.hrtime.bigint();
        const elapsedMs = Number(end - start) / 1000000;
        console.log(`Size ${size.toString().padStart(5)}: ${elapsedMs.toFixed(4).padStart(8)} ms`);
    }
    // Performance test
    const signal = [];
    for (let i = 0; i < 4096; i++) {
        signal.push(new fft_1.Complex(i % 2, 0));
    }
    const start = process.hrtime.bigint();
    const result = (0, fft_1.fft)(signal);
    const end = process.hrtime.bigint();
    const elapsed = Number(end - start) / 1000000000;
    if (elapsed >= 5.0) {
        throw new Error(`FFT of 4096 samples took too long: ${elapsed}s`);
    }
    console.log(`\n✓ Performance test passed (4096 samples: ${(elapsed * 1000).toFixed(4)} ms)`);
    console.log('Test 3: PASSED\n');
    return true;
}
function main() {
    console.log('==================================================');
    console.log('TypeScript FFT Test Suite');
    console.log('==================================================');
    try {
        testCorrectness();
        testEdgeCases();
        testPerformance();
        console.log('==================================================');
        console.log('ALL TESTS PASSED');
        console.log('==================================================');
        process.exit(0);
    }
    catch (error) {
        console.error(`\n❌ Test failed: ${error instanceof Error ? error.message : error}`);
        process.exit(1);
    }
}
// Run tests
main();
