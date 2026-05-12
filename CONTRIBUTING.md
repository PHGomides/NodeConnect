# Padrões de Contribuição - NodeConnect

Para manter a organização, segurança e isolamento dos domínios deste projeto adotamos um controle de versão, onde as contribuições devem seguir as regras abaixo.

## 1. Padrão de Branches (GitHub Flow)

A branch `main` é bloqueada e reflete o código de produção. Todo novo código deve ser feito em uma branch criada a partir da `main`, seguindo o formato:
`tipo/breve-descricao-com-hifens`

**Tipos permitidos:**
* `feat/`: Novas funcionalidades (ex: `feat/tcp-sockets`)
* `fix/`: Correção de bugs (ex: `fix/json-parsing`)
* `refactor/`: Melhorias de código sem alterar o comportamento (ex: `refactor/thread-pool`)
* `test/`: Adição ou modificação de testes (ex: `test/crypto-unit-tests`)
* `chore/`: Manutenção, dependências, configs (ex: `chore/update-bouncycastle`)
* `docs/`: Atualização de documentação (ex: `docs/protocol-specs`)

## 2. Padrão de Commits

Os commits devem ser claros e escritos em **inglês**, no modo imperativo (ex: `add`, `fix`, `remove` — nunca `added`, `fixing`). O formato obrigatório é:
`<tipo>(<escopo>): <descrição breve>`

**Tipos permitidos:** os mesmos das branches (`feat`, `fix`, `refactor`, `test`, `chore`, `docs`).

**Escopos do Projeto:**
* `(app)`: Ponto de entrada e orquestração geral (`Main.java`).
* `(core)`: Modelos de domínio (`MessageEnvelope`) e exceções customizadas.
* `(crypto)`: Bouncy Castle, ECDH, Kyber, AES, gestão de chaves e handshake.
* `(network)`: Sockets, Virtual Threads, IO, length-prefixing.
* `(ui)`: Interface de usuário ou CLI.

**Exemplos:**
* `feat(network): implement length-prefixing for tcp payload`
* `refactor(crypto): migrate ecdh key generation to bouncy castle provider`
* `fix(core): correct timestamp parsing in envelope`
* `test(crypto): add unit tests for AES-GCM and ECDH key exchange`
* `docs: update structure to reflect tests folder`

> **Nota:** commits de escopo amplo (como `docs` de documentação geral) podem omitir o escopo.

## 3. Pull Requests (PRs)

* Nenhum commit vai direto para a `main`.
* Todo PR exige a revisão e aprovação (Approve) obrigatória do outro desenvolvedor.