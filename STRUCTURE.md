# Estrutura de Pastas

Este documento descreve a organização interna da pasta `src/` e onde cada pedaço de código deve ficar.

## 📂 `src/main/java/com/nodeconnect/`

### 📦 `app/`
**O Ponto de Entrada.**

Aqui fica a classe principal (`Main.java`). Ela é responsável por inicializar o sistema, ler as variáveis do arquivo `.env` e costurar a interface com as redes e a criptografia.

### 📦 `core/`
**O Coração do Sistema.**

Regras de negócio puras que não dependem nem de internet, nem de criptografia.
* **`models/`**: Classes de dados e representações estruturais.
    * *Exemplo:* `MessageEnvelope.java`, `UserProfile.java`.
* **`exceptions/`**: Nossos erros customizados para facilitar o debug.
    * *Exemplo:* `InvalidMessageSignatureException.java`, `ConnectionTimeoutException.java`.

### 📦 `crypto/`
**O Domínio de Segurança.**

Nada de redes entra aqui. Esta pasta lida exclusivamente com matemática, geração de chaves, cifras e a biblioteca Bouncy Castle (AES, ECDH, Kyber).
* *Exemplo:* `SecurityService.java` (Interface), `BouncyCastleProvider.java`.

### 📦 `network/`
**O Domínio de Comunicação.**

Nada de criptografia entra aqui. Esta pasta lida exclusivamente com Sockets TCP, Virtual Threads, conversão de bytes para JSON (Length-Prefixing) e comunicação com os IPs do Tailscale.
* *Exemplo:* `P2PNode.java`, `TcpConnectionHandler.java`.

### 📦 `ui/`
**A Interface do Usuário.**

Tudo que interage com quem está usando o chat. Pode ser interações pelo terminal (CLI) ou, futuramente, uma interface gráfica.
* *Exemplo:* `TerminalConsole.java`.