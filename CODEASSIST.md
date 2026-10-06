# ConstruCalc — CodeAssist (Tyron)

Este projeto foi preparado para ser aberto e desenvolvido no **CodeAssist de Tyron12233** em Android.

## Como abrir

1. Abra o CodeAssist.
2. Escolha **Open / Import project**.
3. Selecione a pasta raiz do repositório.
4. Aguarde a sincronização/indexação.
5. Selecione o módulo `app`.
6. Execute a variante `debug` para testar no telefone.

## Estrutura

- `app/src/main/java/com/lay/construcalc/MainActivity.kt` — interface Compose e navegação.
- `app/src/main/java/com/lay/construcalc/model/` — regras dos cálculos.
- `app/src/main/java/com/lay/construcalc/data/` — histórico local.
- `app/src/main/java/com/lay/construcalc/ads/` — AdMob e consentimento.
- `app/src/main/res/` — recursos Android.
- `app/build.gradle.kts` — configuração do módulo Android.

## Regras para continuar o desenvolvimento

- Manter Kotlin + Jetpack Compose + Material 3.
- Evitar plugins Gradle desnecessários.
- Evitar código que dependa exclusivamente do Android Studio.
- Preferir APIs AndroidX/Compose estáveis e dependências Maven declaradas diretamente.
- Não adicionar KSP, Room, Firebase ou outras ferramentas de geração de código sem necessidade.
- Não colocar chaves privadas, keystores ou senhas no repositório.
- Manter `minSdk 24`, `targetSdk 35` e Java 17.
- Preservar o package `com.lay.construcalc`.
- Testar sempre a variante Debug antes de alterar o fluxo de Release.

## Build

O CodeAssist possui um pipeline Android nativo e também uma camada de compatibilidade para projetos Gradle. Por isso os arquivos Gradle deste projeto devem permanecer simples e declarativos.

## Monetização

O AdMob já está integrado. Nunca substituir o App ID ou os IDs de anúncios de produção por IDs de teste em uma build de produção.

## Release

A assinatura de produção continua sendo feita pelo workflow do GitHub Actions. O keystore **não** deve ser commitado no projeto.

