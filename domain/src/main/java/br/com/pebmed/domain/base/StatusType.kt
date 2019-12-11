package br.com.pebmed.domain.base

/**
 * @Descrição: representa os estados de requisição
 */
enum class StatusType(val code: Int) {
    OK(200),
    CREATED(201),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404),
    INTERNAL_SERVER_ERROR(500),
    SERVICE_UNAVAILABLE(503),
    SOCKET_TIMEOUT_EXCEPTION(2100),
    UNKNOWN_HOST_EXCEPTION(2200),
    CONNECT_EXCEPTION(2300),
    NO_ROUTE_TO_HOST_EXCEPTION(2400),
    IO_EXCEPTION(2500),
    NULL_BODY_EXCEPTION(2600),
    DEFAULT_EXCEPTION(2000);

    companion object {
        private val values = values()
        fun getByCode(code: Int): StatusType {
            return values.firstOrNull { it.code == code } ?: DEFAULT_EXCEPTION
        }
    }
}