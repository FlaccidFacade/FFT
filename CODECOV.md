# Codecov Integration Guide

This document describes the Codecov integration for the FFT multi-language project.

## Overview

The project uses [Codecov](https://codecov.io) to track code coverage across six programming languages:
- Python
- C++
- Java
- JavaScript
- TypeScript
- Rust

Coverage reports are automatically generated and uploaded to Codecov on every CI run.

## Configuration

### Repository Setup

1. **Codecov Token**: The repository needs a `CODECOV_TOKEN` secret configured in GitHub Settings → Secrets and variables → Actions
   - Get your token from https://codecov.io after connecting your GitHub repository
   - Add it as a secret named `CODECOV_TOKEN`

2. **codecov.yml**: The configuration file at the root defines:
   - Coverage thresholds and ranges
   - Flags for each language
   - Files to ignore from coverage
   - Comment behavior on pull requests

### Coverage Tools by Language

#### Python
- **Tool**: `coverage.py`
- **Output**: `python/coverage.xml`
- **Local command**: `cd python && coverage run test_fft.py && coverage xml`

#### C++
- **Tools**: `gcov` and `lcov`
- **Output**: `cpp/coverage.info`
- **Local command**: `cd cpp && make coverage`

#### Java
- **Tool**: JaCoCo
- **Output**: `java/jacoco.xml`
- **Local command**: Download JaCoCo, then run tests with the agent and generate report (see CI workflow)

#### JavaScript
- **Tool**: `c8` (native V8 coverage)
- **Output**: `javascript/coverage/lcov.info`
- **Local command**: `cd javascript && npm run coverage`

#### TypeScript
- **Tool**: `c8` (native V8 coverage)
- **Output**: `typescript/coverage/lcov.info`
- **Local command**: `cd typescript && npm run coverage`

#### Rust
- **Tool**: `cargo-llvm-cov`
- **Output**: `rust/lcov.info`
- **Local command**: `cd rust && cargo llvm-cov --lcov --output-path lcov.info`

## CI Workflow Integration

The `.github/workflows/ci.yml` file includes:

1. **Setup**: Install all necessary coverage tools
2. **Test**: Run tests with coverage for each language
3. **Upload**: Upload all coverage reports to Codecov using the `codecov/codecov-action@v4`

### Coverage Upload Step

```yaml
- name: Upload coverage reports to Codecov
  uses: codecov/codecov-action@v4
  with:
    token: ${{ secrets.CODECOV_TOKEN }}
    files: ./python/coverage.xml,./cpp/coverage.info,./java/jacoco.xml,./javascript/coverage/lcov.info,./typescript/coverage/lcov.info,./rust/lcov.info
    flags: python,cpp,java,javascript,typescript,rust
    name: codecov-umbrella
    fail_ci_if_error: false
    verbose: true
```

## Viewing Coverage Reports

1. **Badge**: The README includes a Codecov badge showing overall coverage
2. **Codecov Dashboard**: Visit https://codecov.io/gh/FlaccidFacade/FFT for detailed reports
3. **PR Comments**: Codecov automatically comments on pull requests with coverage changes

## Local Testing

To test coverage generation locally before pushing:

```bash
# Test all languages
cd /home/runner/work/FFT/FFT

# Python
cd python && pip install coverage && coverage run test_fft.py && coverage report

# C++
cd ../cpp && make coverage

# JavaScript
cd ../javascript && npm install && npm run coverage

# TypeScript
cd ../typescript && npm install && npm run coverage

# Rust
cd ../rust && cargo install cargo-llvm-cov && cargo llvm-cov

# Clean up
cd .. && git status  # Coverage files should be ignored
```

## Flags and Carryforward

Each language has its own flag in Codecov:
- `python` - Python coverage
- `cpp` - C++ coverage
- `java` - Java coverage
- `javascript` - JavaScript coverage
- `typescript` - TypeScript coverage
- `rust` - Rust coverage

Carryforward is enabled for all flags to maintain coverage data across builds where specific languages might not have changed.

## Ignored Files

The following files are excluded from coverage:
- Android code (`android/**`)
- Documentation (`**/*.md`)
- Scripts (`**/*.sh`)
- Test files themselves
- Rust main.rs (entry point, not library code)

## Troubleshooting

### Coverage Not Uploading

1. Check that `CODECOV_TOKEN` secret is set correctly
2. Verify coverage files exist in the expected locations
3. Check CI logs for Codecov upload errors
4. Ensure the codecov-action version is up to date

### Low Coverage Percentage

1. Check which files are being excluded in `codecov.yml`
2. Verify that all source files are being tested
3. Review the coverage reports on Codecov dashboard

### Coverage Report Format Issues

1. Ensure each tool generates the correct format (XML for Python/Java, lcov for others)
2. Check that paths in coverage reports are relative to repository root
3. Verify tool versions are compatible with Codecov

## Best Practices

1. **Keep Test Coverage High**: Aim for >70% coverage across all languages
2. **Review Coverage in PRs**: Check Codecov comments before merging
3. **Add Tests with New Code**: Ensure new features include test coverage
4. **Monitor Trends**: Use Codecov dashboard to track coverage over time
5. **Don't Game the System**: Focus on meaningful tests, not just coverage percentage

## References

- [Codecov Documentation](https://docs.codecov.com/)
- [codecov-action GitHub](https://github.com/codecov/codecov-action)
- [Coverage.py Documentation](https://coverage.readthedocs.io/)
- [Lcov Documentation](http://ltp.sourceforge.net/coverage/lcov.php)
- [JaCoCo Documentation](https://www.jacoco.org/jacoco/trunk/doc/)
- [c8 Documentation](https://github.com/bcoe/c8)
- [cargo-llvm-cov Documentation](https://github.com/taiki-e/cargo-llvm-cov)
