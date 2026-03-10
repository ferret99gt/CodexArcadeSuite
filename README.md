# CodexArcadeSuite

Shared-runtime distribution for Codextris, CodexMan, and Codexaga.

## Build

```bash
mvn -Psuite-release -DskipTests package
```

Build from `CodexArcadeSuite/`.

## Output

The shared app image is written to `suite-app/target/dist/CodexArcadeSuite/`.

Launchers included:
- `CodexArcadeSuite.exe`
- `Codextris.exe`
- `CodexMan.exe`
- `Codexaga.exe`
