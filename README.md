# DesafioMuxi

<h1>Geração do APK</h1>

Foi utilizado o Android Studio 2.3.3 para este projeto. O projeto foi testado durante a fase de desenvolvimento com a versão 6.0 do android, num dispositivo físico (Moto G3)

Para construir o APK será necessário instalar no Android Studio algumas ferramentas da SDK. Para isto faça o seguinte:
1. Abra o projeto no Android Studio
2. Selecione Tools -> Android -> SDK Manager
3. Clique na aba SDK Tools 
4. Marque as opções LLDB, CMake e NDK
5. Clique em Apply e depois em OK
6. Quando a instalação completar, clique em Finish e depois em OK

<h1>Execução dos testes</h1>

Os testes estão em java.com.bruno.desafiomuxi (androidTest). Para executar um teste basta clicar no mesmo com o botão direito e então clicar em Run 'nome do teste...'

<h1>Sobre o projeto</h1>

Neste projeto foi utilizado o framework __Volley__ da Google para a a requisição web do arquivo *.json*. O mesmo framework teria sido utilizado para a requisição dos arquivos de imagem, porém a própria Google colocou o módulo de requisição de imagens como obsoleto e não recomenda utilizá-lo, portanto para requisição de imagens foi utilizada a biblioteca __Picasso__. Estas duas bibliotecas têm a vantagem de serem simples e já oferecem __cache__ para as suas requisições.

Para a serialização do arquivo *.json* foi utilizada a biblioteca __Gson__.

Para os testes foi utilizada a biblioteca __JUnit__, pois tem alta integração com o SDK do android, e para auxiliar na instrumentação de alguns testes foi utilizada a biblioteca __Espresso__

O projeto foi organizado em 3 pacotes
* Core: É o pacote que contém as *Activities*
* Currency: É o pacote que contém a classe responsável pelos câmbios entres moedas
* WebRequest: É o pacote que contém o framework de comunicação com a API

As Activities do projeto têm suporte à mudança de orientação das telas sem perder estado. Na __MainAcitivity__ como tem layout único, bastou adicionar no manifest *android:configChanges="orientation|screenSize"*. A __FruitDetailsActivity__ tem layout diferente para cada orientação, e portanto a persistência de estado foi implementada no próprio código.