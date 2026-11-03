# StockScan — Contagem de Estoque (Posto)

Aplicação Android para smartphones Samsung que permite fazer a contagem do
estoque lendo o código de barras dos produtos com a câmera do celular.

Proposta completa e backlog: [`docs/BACKLOG.md`](docs/BACKLOG.md).

## Estado atual

Plano da proposta final: 3 sprints, entrega a **15/12/2026**. Detalhe em [`docs/BACKLOG.md`](docs/BACKLOG.md).

- [x] **US01** – Autenticação: primeiro acesso cria o Administrador, login, perfis, criar e desativar utilizadores
- [x] Base de dados local (Room/SQLite)
- [x] Uso na vertical/horizontal, tablets e smartphones
- [x] Protótipo da leitura de códigos de barras (ecrã "Teste do leitor")
- [ ] **US02** – Gestão de produtos (Sprint 1, até 16/11)
- [ ] **US03** – Leitura de código de barras completa (Sprint 2)
- [ ] **US04 / US05** – Contagem e relatório CSV (Sprint 3)

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
