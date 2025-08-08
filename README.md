# Camel Test Bed

A Spring Boot application with Apache Camel for testing PDF generation and document processing.

## CI/CD and Automated Dependency Management

This project uses GitHub Actions for CI/CD and Renovate bot for automated dependency updates.

### GitHub Actions Workflows

1. **CI Workflow** (`.github/workflows/ci.yml`)
   - Runs on all pushes and pull requests to main/master branches
   - Tests the application using JDK 17
   - Builds the application
   - Uploads test results and build artifacts

2. **Renovate Auto-merge** (`.github/workflows/renovate-auto-merge.yml`)
   - Automatically merges Renovate dependency update PRs when CI passes
   - Comments on PRs when CI fails to alert maintainers

### Renovate Configuration

The `renovate.json` file configures automatic dependency updates:

- **Auto-merge**: Enabled for minor/patch updates and dev dependencies
- **Major updates**: Require manual review (labeled as "major-update")
- **Schedule**: Runs before 6am on weekdays and weekends
- **Limits**: Max 10 concurrent branches, 5 concurrent PRs
- **Required status checks**: CI must pass before auto-merge
- **Notifications**: Assigns and requests review from @RickAnderson-hub

### Features

- ✅ Automatic dependency updates
- ✅ Auto-merge for safe updates (minor/patch)
- ✅ CI validation before merge
- ✅ Alert notifications for failed CI
- ✅ Manual review for major updates
- ✅ Timezone-aware scheduling

## Development

### Prerequisites

- Java 17+
- Gradle (wrapper included)

### Building

```bash
./gradlew build
```

### Testing

```bash
./gradlew test
```

### Running

```bash
./gradlew bootRun
```

## Dependencies

This project uses:
- Spring Boot 3.5.4
- Apache Camel 4.13.0
- H2 Database
- PostgreSQL
- iText PDF
- Lombok