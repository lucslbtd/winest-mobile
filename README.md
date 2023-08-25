# Winest 

Projeto voltado ao desenvolvimento de uma rede social de um aplicativo para os amantes de vinho

# 1. Introdução 
  Esse aplicativo foi criado com o intuito de criar uma rede social voltada para o nicho de apreciadores de vinho e de interessados sobre o assunto, 
  criando uma rede de informações compartilhadas onde os usuários podem trocar experiências, compartilhar conteúdos e utilizar o core da aplicação que
  é a integração com o chat-gpt funcionando como uma recomendação de vinhos, que recebe prompts com base nos desejos e interesses dos usuários.

# 2. Detalhes técnicos do projeto
  A aplicação foi desenvolvida para aplicações Android utilizando Kotlin como linguagem principal. Para requisições na API, está sendo utilizado o retrofit e okHttp
  para o tratamento das informações. A arquitetura utlizada no aplicativo foi MVP, separando em data, domain e presentation. Para a injeção de depêndicas com o processamento
  assíncrono, foi utlizado Koin e corroutines (respectivamente). O backend da aplicação é conectado localmente e será explicado nos tópicos abaixo.

# 3. Passo a passo para executar o projeto
  É necessário possuir a instalação do projeto backend para executar essa aplicação localmente. Clone o projeto https://github.com/sergioluisfilho/winest-server e siga as instruções
  do README. Após isso, caso você esteja rodando a aplicação no seu device fisico, você deve alterar a baseUrl do arquivo NetworkModule para o seu localhost. Faça o mesmo no network_security_config.xml
  
