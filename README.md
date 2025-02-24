# Sistema de Estacionamento

Este projeto é um sistema de gerenciamento de estacionamento que permite gerenciar vagas, reservas e clientes. O sistema oferece funcionalidades como a criação e a finalização de reservas, além da possibilidade de criar, atualizar e excluir vagas de estacionamento. O sistema também lida com a criação e manipulação de clientes, incluindo a verificação de reservas ativas para permitir ou não a exclusão de clientes.

## Tecnologias Utilizadas

- **Backend**: Java 17
- **Framework**: Spring Boot
- **Banco de Dados**: H2 (para testes) e MySQL (para produção)
- **Dependências**: Lombok, JUnit, Mockito
- **Arquitetura**: Camadas (Controller, Service, Repository)
- **Gerenciamento de Dependências**: Maven

## Funcionalidades

1. **Gerenciamento de Vagas**:
   - Criar uma nova vaga de estacionamento.
   - Atualizar uma vaga existente.
   - Excluir uma vaga de estacionamento.
   - Buscar uma vaga por ID.
   - Buscar todas as vagas.

2. **Gerenciamento de Reservas**:
   - Criar uma nova reserva.
   - Finalizar uma reserva.
   - Buscar todas as reservas.
   - Buscar uma reserva por ID.

3. **Gerenciamento de Clientes**:
   - Adicionar um novo cliente.
   - Atualizar os dados de um cliente.
   - Excluir um cliente (desde que não tenha reservas ativas).
   - Buscar todos os clientes.
   - Buscar um cliente por ID.

## Endpoints

- **Vagas**:
  - `POST /parking-spots`: Cria uma nova vaga.
  - `PUT /parking-spots/{id}`: Atualiza uma vaga existente.
  - `DELETE /parking-spots/{id}`: Exclui uma vaga.
  - `GET /parking-spots/{id}`: Recupera uma vaga por ID.
  - `GET /parking-spots`: Recupera todas as vagas.

- **Reservas**:
  - `POST /reservations`: Cria uma nova reserva.
  - `PUT /reservations/{id}/finalize`: Finaliza uma reserva.
  - `GET /reservations`: Recupera todas as reservas.
  - `GET /reservations/{id}`: Recupera uma reserva por ID.

- **Clientes**:
  - `POST /customers`: Adiciona um novo cliente.
  - `PUT /customers/{id}`: Atualiza os dados de um cliente.
  - `DELETE /customers/{id}`: Exclui um cliente.
  - `GET /customers/{id}`: Recupera um cliente por ID.
  - `GET /customers`: Recupera todos os clientes.

## Estrutura do Projeto


src/

├── main/

│   ├── java/

│   │   ├── com/

│   │   │   ├── estacionamento/
│   │   │   │   ├── estacionamento/
│   │   │   │   │   ├── controllers/
│   │   │   │   │   ├── services/
│   │   │   │   │   ├── models/
│   │   │   │   │   ├── repositories/
│   ├── resources/
│   │   ├── application.properties
│   │   ├── logback.xml
├── test/
│   ├── java/
│   │   ├── com/
│   │   │   ├── estacionamento/
│   │   │   │   ├── estacionamento/
│   │   │   │   │   ├── controllers/
│   │   │   │   │   ├── services/



## Como Rodar o Projeto

1. **Clonar o repositório**:

   ```bash
   git clone https://github.com/usuario/projeto-estacionamento.git
   cd projeto-estacionamento

2. **Compilar e rodar com Maven:**

Caso você tenha o Maven instalado, basta rodar o seguinte comando para compilar e rodar o projeto:

mvn spring-boot:run

O projeto estará rodando na URL http://localhost:8080.

3. **Testes Unitários:**

Os testes unitários foram criados utilizando o JUnit e Mockito. Para executá-los, basta rodar o seguinte comando:

4. **Banco de Dados:**

O banco de dados é configurado para usar o H2 em memória durante os testes e o MySQL em produção. As configurações estão localizadas no arquivo src/main/resources/application.properties.

Exceções Tratadas
O sistema lida com várias exceções personalizadas para garantir que erros sejam tratados adequadamente:  
