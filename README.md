# StockScan — Contagem de Estoque (Posto)

Aplicação Android para smartphones Samsung que permite fazer a contagem do
estoque lendo o código de barras dos produtos com a câmera do celular.

Proposta completa e backlog: [`docs/BACKLOG.md`](docs/BACKLOG.md).

## Estado atual — Sprint 1 (US1)

- [x] Base de dados local (Room/SQLite) com usuários, produtos e contagem
- [x] Primeiro acesso: criação da conta de **Administrador**
- [x] Login com usuário e senha (senhas guardadas com hash PBKDF2 + salt)
- [x] Perfis **Administrador** e **Funcionário**
- [x] Administrador cria novos usuários
- [x] Uso na horizontal e em tablets
- [x] **Prova de conceito do leitor de código de barras** (tela "Teste do leitor")
- [ ] US2 – Gestão de produtos (Sprint 2)
- [ ] US3/US4 – Leitura de código de barras e contagem (Sprint 3)
- [ ] US5 – Relatório (Sprint 4)

## Tecnologia

| Item | Escolha |
|------|---------|
| Linguagem | Kotlin |
| Interface | Jetpack Compose (Material 3) |
| Base de dados | Room (SQLite no dispositivo) |
| Android | mínimo 8.0 (API 26), alvo API 35 |
| Leitura de código de barras (Sprint 3) | CameraX + ML Kit |

## Estrutura

```
app/src/main/java/com/posto/stockscan/
├── data/db/          # Tabelas (User, Product, CountEntry, Role), DAOs e AppDatabase
├── data/repository/  # UserRepository, SessionManager
├── domain/           # Regras de validação (sem Android, testáveis)
├── security/         # PasswordHasher
└── ui/               # Tema, navegação e telas (Compose)
```

## Como compilar

Requer Android Studio (ou Android SDK + JDK 17).

```bash
./gradlew testDebugUnitTest
```

```bash
./gradlew assembleDebug
```

O APK fica em `app/build/outputs/apk/debug/`. A cada push, o GitHub Actions
compila o APK e o disponibiliza em **Actions → Android build → Artifacts**
(`StockScan-debug-apk`) — é esse arquivo que se instala no celular para testes.

## Primeiro uso

1. Abra a aplicação: na primeira vez ela pede para criar o Administrador.
2. Com o Administrador, vá em **Usuários → Novo usuário** para criar os funcionários.
3. Os dados ficam guardados no celular e não se perdem ao fechar a aplicação.
