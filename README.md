# ConstruCalc

Aplicativo Android profissional para cálculos de construção civil.

## Calculadoras

- **Blocos:** parede, junta de argamassa, desperdício e desconto de portas/janelas.
- **Blocos no molde:** testa as seis orientações possíveis e mostra distribuição e aproveitamento volumétrico.
- **Concreto:** volume com margem.
- **Área:** área com percentual extra.
- **Volume:** comprimento × largura × altura.
- **Histórico:** guarda até 30 cálculos localmente no dispositivo.

## AdMob

App ID:
ca-app-pub-7506276099130482~3436484957

Interstitial de produção:
ca-app-pub-7506276099130482/5581275875

ID oficial de teste usado automaticamente em DEBUG:
ca-app-pub-3940256099942544/1033173712

O anúncio não aparece na abertura. Os três primeiros cálculos são gratuitos; depois há intervalo mínimo de 5 minutos entre intersticiais. O anúncio só é mostrado depois de um cálculo concluído, nunca durante a introdução dos dados. Se não estiver disponível, o cálculo continua normalmente.

O fluxo de consentimento usa Google UMP antes da inicialização dos anúncios.

## Build

Abra o projeto no Android Studio e sincronize o Gradle.

Requisitos atuais:
- compileSdk 35
- minSdk 24
- JDK 17
- Gradle 8.7 no CI

Os workflows executam build/testes e lint.
