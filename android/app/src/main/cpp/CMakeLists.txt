cmake_minimum_required(VERSION 3.22.1)

project("fft_native")

# Set C++ standard
set(CMAKE_CXX_STANDARD 11)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -O3 -Wall")

# Add the FFT implementation from the repository
add_library(
    fft_native
    SHARED
    fft_jni.cpp
    ../../../../../../cpp/fft_impl.cpp
)

# Include directories
target_include_directories(
    fft_native
    PRIVATE
    ../../../../../../cpp
)

# Link libraries
target_link_libraries(
    fft_native
    android
    log
)
