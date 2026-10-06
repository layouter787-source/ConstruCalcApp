# ConstruCalc

Aplicativo Android moderno para cálculos de construção civil.

## Incluído

- Interface moderna em Jetpack Compose + Material 3.
- Calculadora de blocos.
- Calculadora de concreto.
- Calculadora de área.
- Calculadora de volume.
- Navegação inferior.
- Estrutura preparada para histórico e definições.
- Google AdMob com anúncio intersticial.
- IDs de teste usados automaticamente em DEBUG.
- ID real do AdMob usado apenas em RELEASE.
- Consentimento de privacidade via Google UMP.

## AdMob

App ID:
ca-app-pub-7506276099130482~3436484957

Interstitial:
ca-app-pub-7506276099130482/5581275875

Durante DEBUG é usado o ID oficial de teste do Google para interstitial:
ca-app-pub-3940256099942544/1033173712

O anúncio real só deve ser usado na versão de produção.

## Frequência

O app não mostra interstitial na abertura. Os primeiros 3 cálculos ficam livres. Depois disso, o sistema exige pelo menos 5 minutos entre interstitials e só mostra em uma transição após um cálculo concluído.

Se o anúncio não estiver carregado, o cálculo continua normalmente.

## Build

Abra o projeto no Android Studio e sincronize o Gradle. O workflow do GitHub também executa assembleDebug.

Requisitos usados no projeto: compileSdk 35 e minSdk 24.
