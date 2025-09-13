# Automatically generated CMakeLists, created by the Projucer
# Don't edit this file! Your changes will be overwritten when you re-save the Projucer project!

cmake_minimum_required(VERSION 3.22)

project("AndroidJuceDspInterfaceNative")

add_library("cpufeatures"
    STATIC
    "${ANDROID_NDK}/sources/android/cpufeatures/cpu-features.c"
)
set_source_files_properties("${ANDROID_NDK}/sources/android/cpufeatures/cpu-features.c"
        PROPERTIES
        COMPILE_FLAGS "-Wno-sign-conversion -Wno-gnu-statement-expression"
)

add_definitions(
        [[-DJUCE_ANDROID=1]]
        [[-DJUCE_ANDROID_API_VERSION=24]]
        [[-DJUCE_ANDROID_GL_ES_VERSION_3_0=1]]
        [[-DJUCER_ANDROIDSTUDIO_7F0E4A25=1]]
        [[-DJUCE_APP_VERSION=1.0.0]]
        [[-DJUCE_APP_VERSION_HEX=0x10000]]
)

include_directories( AFTER
    "./JUCE"
    "../../../../external/JUCE/modules"
    "${ANDROID_NDK}/sources/android/cpufeatures"
)

enable_language(ASM)

if(JUCE_BUILD_CONFIGURATION MATCHES "DEBUG")
    set(BINARY_NAME "JuceDsp")
    add_definitions(
            [[-DJUCE_PROJUCER_VERSION=0x80009]]
            [[-DJUCE_MODULE_AVAILABLE_juce_audio_basics=1]]
            [[-DJUCE_MODULE_AVAILABLE_juce_audio_formats=1]]
            [[-DJUCE_MODULE_AVAILABLE_juce_core=1]]
            [[-DJUCE_MODULE_AVAILABLE_juce_data_structures=1]]
            [[-DJUCE_MODULE_AVAILABLE_juce_dsp=1]]
            [[-DJUCE_MODULE_AVAILABLE_juce_events=1]]
            [[-DJUCE_GLOBAL_MODULE_SETTINGS_INCLUDED=1]]
            [[-DJUCE_STRICT_REFCOUNTEDPOINTER=1]]
            [[-DJUCE_STANDALONE_APPLICATION=0]]
            [[-DJUCER_ANDROIDSTUDIO_7F0E4A25=1]]
            [[-DJUCE_APP_VERSION=1.0.0]]
            [[-DJUCE_APP_VERSION_HEX=0x10000]]
            [[-DDEBUG=1]]
            [[-D_DEBUG=1]]
    )
elseif(JUCE_BUILD_CONFIGURATION MATCHES "RELEASE")
    set(BINARY_NAME "JuceDsp")
    add_definitions(
            [[-DJUCE_PROJUCER_VERSION=0x80009]]
            [[-DJUCE_MODULE_AVAILABLE_juce_audio_basics=1]]
            [[-DJUCE_MODULE_AVAILABLE_juce_audio_formats=1]]
            [[-DJUCE_MODULE_AVAILABLE_juce_core=1]]
            [[-DJUCE_MODULE_AVAILABLE_juce_data_structures=1]]
            [[-DJUCE_MODULE_AVAILABLE_juce_dsp=1]]
            [[-DJUCE_MODULE_AVAILABLE_juce_events=1]]
            [[-DJUCE_GLOBAL_MODULE_SETTINGS_INCLUDED=1]]
            [[-DJUCE_STRICT_REFCOUNTEDPOINTER=1]]
            [[-DJUCE_STANDALONE_APPLICATION=0]]
            [[-DJUCER_ANDROIDSTUDIO_7F0E4A25=1]]
            [[-DJUCE_APP_VERSION=1.0.0]]
            [[-DJUCE_APP_VERSION_HEX=0x10000]]
            [[-DNDEBUG=1]]
    )
else()
    message( FATAL_ERROR "No matching build-configuration found." )
endif()

file(GLOB_RECURSE PROJECT_SOURCES_1
        "../../../../external/JUCE/modules/juce_core/*.cpp"
        "../../../../external/JUCE/modules/juce_core/*.h"
        "../../../../external/JUCE/modules/juce_core/*.mm"
        "../../../../external/JUCE/modules/juce_audio_basics/*.cpp"
        "../../../../external/JUCE/modules/juce_audio_basics/*.h"
        "../../../../external/JUCE/modules/juce_audio_basics/*.mm"
        "../../../../external/JUCE/modules/juce_audio_formats/*.cpp"
        "../../../../external/JUCE/modules/juce_audio_formats/*.h"
        "../../../../external/JUCE/modules/juce_audio_formats/*.mm"
        "../../../../external/JUCE/modules/juce_data_structures/*.cpp"
        "../../../../external/JUCE/modules/juce_data_structures/*.h"
        "../../../../external/JUCE/modules/juce_data_structures/*.mm"
        "../../../../external/JUCE/modules/juce_dsp/*.cpp"
        "../../../../external/JUCE/modules/juce_dsp/*.h"
        "../../../../external/JUCE/modules/juce_dsp/*.mm"
        "../../../../external/JUCE/modules/juce_events/*.cpp"
        "../../../../external/JUCE/modules/juce_events/*.h"
        "../../../../external/JUCE/modules/juce_events/*.mm"

        "JUCE/JuceHeader.h"
)

file(GLOB_RECURSE PROJECT_SOURCES_2
        "JUCE/include_juce_audio_basics.cpp"
        "JUCE/include_juce_audio_formats.cpp"
        "JUCE/include_juce_core.cpp"
        "JUCE/include_juce_core_CompilationTime.cpp"
        "JUCE/include_juce_data_structures.cpp"
        "JUCE/include_juce_dsp.cpp"
        "JUCE/include_juce_events.cpp"
)

add_library( ${BINARY_NAME}
        SHARED
        "main.cpp"
        "JuceNativeJniBridge.h"
        "JuceNativeJniBridge.cpp"
        "TenBrandEQ.cpp"
        ${PROJECT_SOURCES_1}
        ${PROJECT_SOURCES_2}
)

set_source_files_properties(
    ${PROJECT_SOURCES_1}
    PROPERTIES
    HEADER_FILE_ONLY TRUE
)

if( JUCE_BUILD_CONFIGURATION MATCHES "DEBUG" )
    target_compile_options( ${BINARY_NAME} PRIVATE "-fsigned-char" )
endif()

if( JUCE_BUILD_CONFIGURATION MATCHES "RELEASE" )
    target_compile_options( ${BINARY_NAME} PRIVATE "-fsigned-char" )
endif()

find_library(log "log")
find_library(android "android")
find_library(glesv3 "GLESv3")
find_library(egl "EGL")

message(STATUS "Library location"
        "${log}"
        ${android}
        ${glesv3}
        ${egl}
)

target_link_libraries( ${BINARY_NAME}

    ${log}
    ${android}
    ${glesv3}
    ${egl}
    "cpufeatures"
)
