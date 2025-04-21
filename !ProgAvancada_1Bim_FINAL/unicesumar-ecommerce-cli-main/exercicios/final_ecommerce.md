# Exercício: Registro de Venda no Sistema de E-commerce (Java - Linha de Comando)

## Contexto
Nossa aplicação de e-commerce já conta com o cadastro de usuários e produtos. Agora, você deve implementar a funcionalidade de **venda de produtos**, considerando a interação entre o cliente, os produtos e a forma de pagamento.

---

## Objetivo
Implementar uma funcionalidade de venda que:
1. Busque um **usuário existente** no banco de dados (por e-mail).
2. Busque um ou mais **produtos existentes** no banco de dados (por ID).
3. Permita ao usuário escolher uma **forma de pagamento** dentre as opções disponíveis.
4. Registre a venda, associando o usuário, os produtos escolhidos e a forma de pagamento.

---

## Requisitos Funcionais

- O sistema deve permitir que o operador (via linha de comando) informe:
  - Email do usuário comprador
  - IDs dos produtos que deseja comprar
  - Forma de pagamento (ex: Cartão de Crédito, Boleto, PIX)

- O sistema deve validar:
  - Se o usuário existe
  - Se os produtos existem
  - Se a forma de pagamento é válida

- O sistema deve exibir um resumo da venda com:
  - Nome do cliente
  - Lista de produtos (nome + preço)
  - Valor total
  - Forma de pagamento escolhida

- A venda deve ser armazenada em banco de dados SQLite com os dados mencionados.

---

## Exemplo de Execução
```
Digite o Email do usuário: rafael@example.com
Usuário encontrado: João da Silva

Digite os IDs dos produtos (separados por vírgula): 1,3  
Produtos encontrados:  
- Camiseta (R$ 50.00)  
- Tênis (R$ 200.00)  

Escolha a forma de pagamento:  
1 - Cartão de Crédito  
2 - Boleto  
3 - PIX  
Opção: 3

Aguarde, efetuando pagamento...
Pagamento confirmado com sucesso via PIX. Chave de Autenticação: 12345678-1234-1234-12345678

Resumo da venda:  
Cliente: João da Silva  
Produtos:  
- Camiseta  
- Tênis  
Valor total: R$ 250.00  
Pagamento: PIX  

Venda registrada com sucesso!
```

---

## Requisitos Técnicos

- Utilizar banco de dados SQLite juntamente com a conexão JDBC
- O pagamento do produto deve utilizar **obrigatóriamente** o Design Pattern _Strategy_ e _Factory_ como visto em sala de aula.
- A forma de pagamento pode ser um enum, mas não é um requisito obrigatório:
```java
enum FormaPagamento {
    CARTAO,
    BOLETO,
    PIX
}
```

