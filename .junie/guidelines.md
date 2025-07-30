# Annette Project Development Guidelines

## Build/Configuration Instructions

### Project Structure
- **Multi-module Android project** with Gradle Kotlin DSL
- **Main app module**: `app/` - Uses Jetpack Compose with Material3
- **Feature module**: `feature/entry/` - Uses traditional Android Views
- **Version catalog**: `gradle/libs.versions.toml` for centralized dependency management

### Java Version Configuration
⚠️ **Critical**: The project requires **Java 21** compatibility. Using Java 23 will cause runtime errors during testing.

Current configuration:
- **App module**: Java 21 (fixed from Java 23)
- **Feature/entry module**: Java 11 (inconsistent - should be updated to Java 21)

### Build Requirements
- **Minimum SDK**: 26
- **Target SDK**: 36
- **Compile SDK**: 36
- **Gradle JVM Args**: `-Xmx2048m -Dfile.encoding=UTF-8`

### Key Dependencies
- **Kotlin**: 2.2.0
- **Android Gradle Plugin**: 8.11.1
- **Compose BOM**: 2025.07.00
- **AndroidX Core**: 1.16.0

## Testing Information

### Test Structure
The project uses standard Android testing frameworks:

#### Unit Tests (`src/test/`)
- **Framework**: JUnit 4.13.2
- **Location**: `{module}/src/test/java/{package}/`
- **Purpose**: Local tests that run on the development machine

#### Instrumented Tests (`src/androidTest/`)
- **Framework**: AndroidJUnit4 with Espresso
- **Location**: `{module}/src/androidTest/java/{package}/`
- **Purpose**: Tests that run on Android devices/emulators

### Running Tests

#### Command Line
```bash
# Run all tests in a module
./gradlew :app:test                    # Unit tests
./gradlew :app:connectedAndroidTest    # Instrumented tests

# Run specific test class
./gradlew :app:test --tests "com.softklass.annette.DemoTest"

# Run specific test method
./gradlew :app:test --tests "com.softklass.annette.DemoTest.stringManipulation_isCorrect"
```

#### Android Studio
- Right-click on test class/method → "Run"
- Use the green arrow icons in the gutter

### Adding New Tests

#### Unit Test Example
```kotlin
class MyFeatureTest {
    @Test
    fun myFunction_withValidInput_returnsExpectedResult() {
        // Arrange
        val input = "test input"

        // Act
        val result = myFunction(input)

        // Assert
        assertEquals("expected output", result)
    }
}
```

#### Instrumented Test Example
```kotlin
@RunWith(AndroidJUnit4::class)
class MyInstrumentedTest {
    @Test
    fun useAppContext() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.softklass.annette", appContext.packageName)
    }
}
```

### Test Execution Verification
✅ **Verified**: Demo tests run successfully with the corrected Java 21 configuration.

## Additional Development Information

### Code Style
- **Kotlin Code Style**: Official (configured in `gradle.properties`)
- **Package Structure**: 
  - App: `com.softklass.annette`
  - Feature/entry: `com.samples.entry` (inconsistent naming)

### Module Configuration Issues
⚠️ **Inconsistencies to Address**:
1. **Java versions**: App uses Java 21, feature/entry uses Java 11
2. **Module types**: Both modules are configured as applications instead of feature/entry being a library
3. **Package naming**: Inconsistent package prefixes between modules

### Gradle Configuration
- **Repository management**: Centralized in `settings.gradle.kts`
- **Dependency resolution**: `FAIL_ON_PROJECT_REPOS` mode enabled
- **AndroidX**: Enabled with non-transitive R class optimization
- **Parallel builds**: Available but commented out

### Development Recommendations
1. **Standardize Java version** across all modules to Java 21
2. **Consider converting** `feature/entry` from application to Android library module
3. **Unify package naming** convention across modules
4. **Enable parallel builds** for faster compilation: `org.gradle.parallel=true`

### Build Commands
```bash
# Clean build
./gradlew clean

# Build all modules
./gradlew build

# Build specific module
./gradlew :app:build
./gradlew :feature:entry:build

# Install debug APK
./gradlew :app:installDebug
```
