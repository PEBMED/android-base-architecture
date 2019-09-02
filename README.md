[![codebeat badge](https://codebeat.co/badges/38f811e1-c9b4-45d1-9673-0c4e890d086a)](https://codebeat.co/projects/github-com-pebmed-android-base-architecture-master)
![Code Climate maintainability](https://img.shields.io/codeclimate/maintainability-percentage/PEBMED/android-base-architecture)

## **Arquitetura para projetos Android**
>  CLEAN ARCH - KOIN - ROOM - RETROFIT - COROUTINES - MVVM

Consumo da [API do GitHub](https://developer.github.com/v3/search/) para:
- Listar repositórios
- Listar pull requests
- Listar issues

## Motivação
- Organização
- Facilidade para debugar
- Facilidade para lidar com ciclo de vidas
- Modularização
- Testes

A partir de uma [proposta inicial](https://github.com/luisfernandezbr/github-api-android-client/tree/develop) realizada por um time externo, decidimos continuar a implementação e tornar o projeto _open source_ para contribuir com a comunidade.

## Como funciona
Acompanhe nossa [Wiki]() para entender a estrutura (TODO).

## Libs
* [ktx](https://developer.android.com/kotlin/ktx)
* [gson](https://github.com/google/gson)
* [glide](https://github.com/bumptech/glide)
* [retrofit](https://github.com/square/retrofit)
* [okttp logging interceptor](https://github.com/square/okhttp/tree/master/okhttp-logging-interceptor)
* [koin](https://insert-koin.io/)
* [viewModel + liveData](https://developer.android.com/topic/libraries/architecture/adding-components)
* [room](https://developer.android.com/topic/libraries/architecture/room)
* [circleImageView](https://github.com/hdodenhof/CircleImageView)

## Roadmap
- [ ] Paginar resultados
- [ ] Listar pull requests
- [ ] Listar issues
- [ ] Buscar conteúdo local como fonte principal e dar opção de buscar remoto
- [ ] Favoritar conteúdo no banco de dados local
- [ ] Testes não instrumentados
- [ ] Testes instrumentados
- [ ] Modularizar

## Licença
Este projeto está licenciado sob a licença MIT - veja o arquivo [LICENSE.md](LICENSE) para detalhes.
