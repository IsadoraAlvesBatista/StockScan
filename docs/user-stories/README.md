# User stories — StockScan

Uma ficha por user story, no formato usado no quadro (critérios de aceitação,
dependências, requisitos não funcionais, notas, tarefas, DoR e DoD).
Fonte: proposta final enviada à cliente (2/11/2026). Estado geral: [`../BACKLOG.md`](../BACKLOG.md).

| US | Título | Sprint | Estado |
|----|--------|--------|--------|
| [US01](US01.md) | Autenticação de utilizadores e base de dados | 1 (02/11 – 16/11) | ✅ Desenvolvida — falta validação da cliente |
| [US02](US02.md) | Gestão de produtos | 1 (02/11 – 16/11) | ⏳ Por fazer |
| [US03](US03.md) | Leitura de código de barras | 2 (17/11 – 30/11) | 🟡 Protótipo |
| [US04](US04.md) | Contagem de produtos | 3 (01/12 – 15/12) | ⏳ Por fazer |
| [US05](US05.md) | Relatório de contagem | 3 (01/12 – 15/12) | ⏳ Por fazer |

## Requisitos não funcionais (comuns a todas as US)

- **NFR01 – Funcionamento offline:** nenhuma funcionalidade precisa de internet.
- **NFR02 – Persistência local:** os dados ficam numa base de dados no dispositivo (Room/SQLite) e mantêm-se depois de fechar ou reiniciar a aplicação; alterações ao esquema usam migrações, sem perda de dados.
- **NFR03 – Segurança:** acesso com senha; senhas guardadas como hash (PBKDF2 com salt); cada perfil só vê as funções que lhe estão destinadas.
- **NFR04 – Compatibilidade:** tablets Samsung com Android 8.0 ou superior; também smartphones; vertical e horizontal.
- **NFR05 – Língua:** interface em português de Portugal.
- **NFR06 – Qualidade do código:** Kotlin, uma classe por ficheiro, KDoc nas classes, testes unitários da lógica, build automático (GitHub Actions).

## Definition of Ready (comum)
- [ ] Critérios de aceitação claros e definidos
- [ ] Consistente com a proposta enviada à cliente
- [ ] Dependências identificadas
- [ ] Sprint e prazo definidos
- [ ] Registada no backlog

## Definition of Done (comum)
- [ ] Cumpre todos os critérios de aceitação (se for acrescentado um critério novo, a US volta a desenvolvimento)
- [ ] Compila no GitHub Actions (testes unitários + APK)
- [ ] Testes unitários das regras de negócio a passar
- [ ] Testada num tablet Samsung real
- [ ] Validada pela cliente na reunião de fim de sprint
- [ ] Backlog atualizado com o estado e a descrição
- [ ] Código documentado (KDoc) e organizado (uma classe por ficheiro)
- [ ] A responsável consegue explicar como a US funciona e responder a perguntas sobre ela
