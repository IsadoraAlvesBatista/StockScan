# Backlog — StockScan

Fonte: **proposta final enviada à cliente** (*Proposta — Aplicação de Contagem de
Estoque*, 2 de novembro de 2026). Valores e condições comerciais não estão neste
repositório.

- **Cliente:** posto de combustível (dados da cliente na proposta, fora do repositório)
- **Plataforma:** Android — tablets Samsung (funciona também em smartphones), Android 8.0+
- **Sprints de 2 semanas**, reunião quinzenal, versão de teste (APK) no fim de cada sprint
- **Prazo final:** 15/12/2026

## Planeamento

| Sprint | Período | Entregas | Estado |
|--------|---------|----------|--------|
| Sprint 1 | 02/11 – 16/11/2026 | US01 Autenticação + base de dados · US02 Gestão de produtos | 🟡 Em curso |
| Sprint 2 | 17/11 – 30/11/2026 | US03 Leitura do código de barras | 🟡 Protótipo pronto |
| Sprint 3 | 01/12 – 15/12/2026 | US04 Contagem · US05 Relatório, testes finais e ajustes | ⏳ |

> Atenção: 01/12 e 08/12 são feriados — o Sprint 3 tem 9 dias úteis.

## User stories e critérios de aceitação

Legenda: ✅ feito · 🟡 parcial · ⏳ por fazer

### US01 — Autenticação de utilizadores (Sprint 1) ✅
*Como utilizador, quero entrar na aplicação com nome e senha, para que só pessoas autorizadas a possam usar.*
- ✅ Dois perfis: Administrador e Funcionário.
- ✅ No primeiro acesso é criada a conta de Administrador.
- ✅ O Administrador pode criar novos utilizadores.
- ✅ O Administrador pode desativar utilizadores; um utilizador desativado deixa de poder entrar, mas o seu nome continua nos relatórios antigos.
  (Regras extra: não é possível desativar a própria conta nem o último Administrador ativo; a conta pode ser reativada.)
- ✅ As senhas são guardadas de forma protegida (hash PBKDF2 com salt).

### US02 — Gestão de produtos (Sprint 1) ⏳
*Como utilizador, quero consultar e registar produtos, para manter a lista do posto atualizada.*
- ⏳ A aplicação já vem com a lista de produtos base, fornecida pela cliente em Excel ou CSV (**prazo da cliente: 09/11/2026**).
- ⏳ Pesquisa de produtos por nome ou por código de barras.
- 🟡 É possível inserir novos produtos (**nome e código de barras**). Hoje só existe o registo rápido no protótipo do leitor.
- ✅ Não é possível registar dois produtos com o mesmo código de barras (índice único na base de dados).
- ✅ Os dados ficam guardados no dispositivo e não se perdem ao fechar a aplicação.

### US03 — Leitura de código de barras (Sprint 2) 🟡
*Como funcionário, quero ler o código de barras com a câmara, para identificar o produto sem escrever.*
- ✅ O produto lido é identificado automaticamente, sem necessidade de internet (CameraX + ML Kit, no protótipo "Teste do leitor").
- ✅ Se o código não existir, a aplicação avisa.
- ⏳ Se o código estiver danificado, é possível escrevê-lo à mão ou procurar o produto pelo nome.
- Extra já feito (não exigido na proposta): lanterna, bip ao ler.

### US04 — Contagem de produtos (Sprint 3) ⏳
*Como funcionário, quero indicar a quantidade de cada produto lido, para registar o estoque existente.*
- ⏳ Após ler o código, o funcionário escreve a quantidade existente.
- ⏳ Se o mesmo produto for lido de novo, a aplicação mostra a quantidade já registada e permite alterá-la.
- ⏳ A contagem fica registada, mesmo que a aplicação seja fechada, até o relatório ser gerado, e pode ser alterada até lá.
  (Tabela `count_entries` já existe: uma linha por produto, com utilizador e data/hora.)

### US05 — Relatório de contagem (Sprint 3) ⏳
*Como utilizador, quero gerar um relatório da contagem, para saber o estoque existente.*
- ⏳ Gera um ficheiro **CSV** (abre no Excel) com: produto, código de barras, quantidade, data e hora e utilizador que fez a contagem.
- ⏳ O ficheiro pode ser guardado no tablet ou partilhado (por exemplo, por e-mail ou WhatsApp).
- ⏳ Os relatórios gerados ficam guardados no dispositivo para consulta (precisa de novas tabelas → migração da base de dados para a versão 2).
- ⏳ Antes de gerar, a aplicação pede confirmação; depois, a contagem é reposta a zero para um novo ciclo.

## Requisitos técnicos (da proposta)
- Funcionamento **offline**; dados numa base de dados no dispositivo (Room/SQLite).
- Cada tablet tem a sua própria lista e contagem (sem sincronização).
- Câmara usada só para ler códigos; o Android pede autorização na primeira utilização; nenhuma imagem é gravada ou enviada.
- Vertical e horizontal; tablets e smartphones.

## Fora do âmbito
Sincronização/nuvem · integração com faturação ou gestão · iOS/computador · custos de terceiros (conta Google Play).

## Decisões pendentes (primeira reunião)
- [ ] Forma de instalação: Google Play privada **ou** APK assinado.
- [ ] Receção da lista de produtos (até 09/11) e o seu formato.
- [ ] Data da primeira reunião quinzenal.

## Registo de decisões
_(preencher após cada reunião)_
