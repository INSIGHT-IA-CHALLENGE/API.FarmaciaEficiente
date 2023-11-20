# Farmacia.Eficiente
Uma ferramenta para gerenciar e otimizar as filas de espera nos postos de saude para a retirada de medicamentos

## Grupo
- Gustavo Balero RM93090
- Marcelo Gimenes RM93897
- Pedro Vidal RM93567

## Banco de Dados
Para criar as tabelas e estruturas necessárias do banco de dados, rodar o script do arquivo *create.sql* localizado na raiz do projeto

## Variaveis de Ambiente
Essas variaveis de ambiente são **obrigatórias** para uso da API e deploy para produção

| Nome | Valor | Obrigatório |
|------|-------|-------------|
| ACTIVE_PROFILE | Possíveis valores: dev ou prod | Não |
| DB_USER_FIAP | Username do banco de dados ORACLE | Somente caso ACTIVE_PROFILE tenha o valor prod |
| DB_PASSWORD_FIAP | Senha do banco de dados ORACLE | Somente caso ACTIVE_PROFILE tenha o valor prod |

## Endpoints
Para testar a API de forma mais fácil, disponibilizamos um arquivo *Endpoints.json* com as rotas e outros pré requisitos já configurados, o arquivo pode ser importado no programa **Insomnia** ou **Postman**