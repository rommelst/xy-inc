# XY-Inc

A **XY Inc.** é uma empresa especializada na produção de excelentes receptores GPS (Global
Positioning System). A diretoria está empenhada em lançar um dispositivo inovador que
promete auxiliar pessoas na localização de ponto de interesse (POIs), e precisa muito de sua
ajuda.


# Instalação
### Baixar, testar e executar
> git clone https://github.com/rommelst/xy-inc.git

> mvn clean test

> mvn spring-boot:run


# Requisições


## 1. Consultar/Listar pontos de interesse já cadastrados 
Todas requisições de consulta/listagem usam o método GET do HTTP.
Código de retorno: 200 - OK
### Consultas sem paginação

Para listar múltiplos pontos de interesse:
> http://localhost:8080/v1/pontointeresse : Retorna todos os pontos de interesse cadastrados na base de dados.

Para consultar somente um ponto de interesse:
> http://localhost:8080/v1/pontointeresse/{id} : Retorna o ponto de interesse de id {id}.

Exemplo:

	[GET] http://localhost:8080/v1/pontointeresse/1

### Listagem com paginação
> http://localhost:8080/v1/pontointeresse/page?[parâmetros] : Retorna todos os pontos de interesse cadastrados na base de dados com base nos parâmetros informados.

Parâmetros:
**page**: se informado retorna a página desejada. Zero (0) é a primeira página.
**size**: define a quantidade de pontos de interesse que serão retornados na página. O valor padrão é vinte (20).
**sort**: ordena os pontos de interesse pela chave informada. O valores possíveis são: nomePontoInteresse | coordenadaX | coordenadaY.

Exemplo:

	[GET] http://localhost:8080/v1/pontointeresse/page?page=2&size=10&sort=nomePontoInteresse

### Busca por proximidade
Nesse tipo de busca a camada service inicialmente limita a busca ao quadrilátero formado pela relação entre a corrdenada fornecida e a distância máxima. Após receber os pontos de interesse desse quadrilátero, aqueles que não estão no raio de distância da coordenada informada são excluídos da lista inicial.
> http://localhost:8080/v1/pontointeresse/near?[parâmetros] : Retorna todos os pontos de interesse cadastrados na base de dados com base nos parâmetros informados.

**x**:  parâmetro obrigatório que informa a coordenada X.
**y**:  parâmetro obrigatório que informa a coordenada Y.
**d**:  parâmetro obrigatório que informa a distância máxima dos pontos de interesse em relação à coordenada informada nos parâmetros x e y.

Exemplo:

	[GET] http://localhost:8080/v1/pontointeresse/near?x=20&y=10&d=10

### Busca por proximidade com paginação
Nessa busca, a camada service passa para a camada de persistência todos os parâmetros necessários para que o banco de dados possa limitar o quadrilátero e então definir os pontos de interesse que estão dentro da distância máxima da coordenada informada.
> http://localhost:8080/v1/pontointeresse/near/page?[parâmetros] : Retorna todos os pontos de interesse cadastrados na base de dados que estão dentro da distância máxima da coordenada de referência.

**Parâmetros**: São os mesmos parâmetros da **busca por proximidade**  mais os parâmetros de paginação.

Exemplo:

	[GET] http://localhost:8080/v1/pontointeresse/near/page?x=20&y=10&d=10&page=2&size=10&sort=nomePontoInteresse


## 2. Cadastrar novo ponto de interesse
Usar o método POST do HTTP.
Código de retorno: 201 - Created
Url: O servidor retornará no **header location** a URL para acessar o novo recurso

> http://localhost:8080/v1/pontointeresse : Cadastra no banco de dados o ponto de interesse enviado no corpo da requisição.

Exemplo:

	[POST] http://localhost:8080/v1/pontointeresse
	[HEADER] Content-Type: application/json
	{
		"nomePontoInteresse": "Novo Registro"
		"coordenadaX": 27,
		"coordenadaY": 12,
	}

## 3. Alterar ponto de interesse existente
Usar o método PUT do HTTP. Esse serviço checa se o id informad na URL e no corpo da requisição são o mesmo.

> http://localhost:8080/v1/pontointeresse/{id} : Altera no banco de dados o ponto de interesse enviado no corpo da requisição.

Exemplo:

	[PUT] http://localhost:8080/v1/pontointeresse/{id}
	[HEADER] Content-Type: application/json
	{
		"id": {id}
		"nomePontoInteresse": "Nome alterado do ponto de interesse"
		"coordenadaX": 27,
		"coordenadaY": 12,
	}

## 4. Apagar um ponto de interesse já cadastrado
Usar o método DELETE do HTTP. Esse serviço checa se o id informad na URL e no corpo da requisição são o mesmo. 

> http://localhost:8080/v1/pontointeresse/{id} : Apaga do banco de dados o ponto de interesse enviado no corpo da requisição.

Exemplo:

	[DELETE] http://localhost:8080/v1/pontointeresse/{id}


# Teste de integração
### PontoInteresseControllerTest
Nesse arquivo de teste, optei por fazer o teste de integração usando o banco de dados H2. Por isso, os *tests cases* que deveriam ter como ponto inicial as linhas de código do arquivo PontoInteresseController.java tiveram seu escopo expandido e passaram a abranger também a camada de serviços e de persistência.

### PontoInteresseServiceTest
Nesse arquivo de teste, optei por usar o Mockito para fazer o mock do repositório. Com isso o teste manteve seu foco nos requisitos de funcionalidade do pacote PontoInteresseService.