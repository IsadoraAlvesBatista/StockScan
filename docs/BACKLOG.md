# Backlog — StockScan

Baseado na *Proposta de Projeto — Aplicação de Contagem de Estoque* (02/11/2026).

Metodologia: sprints de até 2 semanas, com versão funcional entregue no fim de
cada sprint e reunião quinzenal com o cliente.

## User stories

| ID | Funcionalidade | Critérios de aceitação | Sprint | Estado |
|----|----------------|------------------------|--------|--------|
| US1 | Autenticação de usuários | Login com nome e senha · Perfis Administrador e Funcionário · Administrador cria usuários | 1 | ✅ Implementado |
| US2 | Gestão de produtos | App já vem com a lista de produtos do cliente · Inserir novos produtos (nome, código de barras, etc.) · Dados persistem no dispositivo | 2 | ⏳ |
| US3 | Leitura de código de barras | Leitura com a câmera · Produto identificado automaticamente · Aviso se o código não existir | 3 | ⏳ |
| US4 | Contagem de produtos | Após ler o código, digitar a quantidade · Contagem fica registrada até gerar o relatório | 3 | ⏳ |
| US5 | Relatório de contagem | Lista produtos lidos e quantidades · Após gerar, a contagem é zerada | 4 | ⏳ |

## Sprint 1 — detalhes de implementação

- **Primeiro acesso:** sem usuários na base, a app pede a criação do Administrador
  (não existe senha padrão de fábrica).
- **Usuário** não diferencia maiúsculas/minúsculas; só letras, números, `.`, `-`, `_`.
  Senha com pelo menos 6 caracteres.
- **Sessão** fica só em memória: ao fechar a app é preciso entrar de novo.
- **Base de dados** (versão 1): tabelas `users`, `products` e `count_entries`
  já criadas para os sprints seguintes. Alterações de esquema serão feitas com
  migrações, sem perder dados.

## Pendências com o cliente

- [ ] Lista inicial de produtos (nome e código de barras) — necessária para o Sprint 2.
- [ ] Formato do relatório (tela, PDF, partilha por WhatsApp/e-mail?) — Sprint 4.
- [ ] Quantidades fracionadas (ex.: litros) ou só unidades inteiras? — Sprint 3.
- [ ] Prazo final e data de início do Sprint 1.
