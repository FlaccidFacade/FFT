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
	rm -f fft test_fft *.o

.PHONY: all clean
