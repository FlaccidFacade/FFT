CXX = g++
CXXFLAGS = -std=c++11 -Wall -Wextra -O2

all: fft test_fft

# Build object file for FFT implementation
fft_impl.o: fft_impl.cpp fft.h
	$(CXX) $(CXXFLAGS) -c fft_impl.cpp -o fft_impl.o

# Build main executable
fft: fft.cpp fft_impl.o fft.h
	$(CXX) $(CXXFLAGS) -o fft fft.cpp fft_impl.o

# Build test executable
test_fft: test_fft.cpp fft_impl.o fft.h
	$(CXX) $(CXXFLAGS) -o test_fft test_fft.cpp fft_impl.o

clean:
	rm -f fft test_fft *.o *.gcda *.gcno *.info

# Coverage target
coverage: clean
	$(CXX) -std=c++11 --coverage -O0 -c fft_impl.cpp -o fft_impl.o
	$(CXX) -std=c++11 --coverage -O0 -o test_fft test_fft.cpp fft_impl.o
	./test_fft
	lcov --capture --directory . --output-file coverage.info --rc branch_coverage=1 --ignore-errors mismatch
	lcov --remove coverage.info '/usr/*' --output-file coverage.info
	lcov --list coverage.info

.PHONY: all clean coverage
