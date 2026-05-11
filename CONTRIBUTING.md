# Padrões de Contribuição - NodeConnect

Para manter a organização, segurança e isolamento dos domínios deste projeto adotamos um controle de versão, onde as contribuições devem seguir as regras abaixo.

## 1. Padrão de Branches (GitHub Flow)

A branch `main` é bloqueada e reflete o código de produção. Todo novo código deve ser feito em uma branch criada a partir da `main`, seguindo o formato:
`tipo/breve-descricao-com-hifens`

**Tipos permitidos:**
* `feat/`: Novas funcionalidades (ex: `feat/tcp-sockets`)
* `fix/`: Correção de bugs (ex: `fix/json-parsing`)
* `refactor/`: Melhorias de código sem alterar o comportamento (ex: `refactor/thread-pool`)
* `chore/`: Manutenção, dependências, configs (ex: `chore/update-xxxx`)
* `docs/`: Atualização de documentação (ex: `docs/protocol-specs`)

## 2. Padrão de Commits

Os commits devem ser claros e escritos em **inglês**. O formato obrigatório é:
`<tipo>(<escopo>): <descrição breve>`

**Escopos do Projeto:**
* `(network)`: Sockets, Virtual Threads, IO.
* `(crypto)`: Bouncy Castle, ECDH, Kyber, AES.
* `(core)`: Modelos de domínio como `MessageEnvelope`.
* `(ui)`: Interface de usuário ou CLI.

**Exemplos:**
* `feat(network): implement length-prefixing for tcp payload`
* `refactor(crypto): migrate ecdh key generation to bouncy castle provider`
* `fix(core): correct timestamp parsing in envelope`

## 3. Pull Requests (PRs)
* Nenhum commit vai direto para a `main`.
* Todo PR exige a revisão e aprovação (Approve) obrigatória do outro desenvolvedor.
