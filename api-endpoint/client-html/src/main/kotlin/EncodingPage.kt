

/*
external interface PageProps : Props {
    var decodedString: String
    var encodedString: String
}

data class PageState(val decodedString: String, val encodedString: String) : State

private val scope = MainScope()

class EncodingPage(props: PageProps) : RComponent<PageProps, PageState>(props) {

    init {
        state = PageState(
            props.decodedString,
            props.encodedString,
        )
    }

    private suspend fun onClickEncode() {
        val encodingEndpoint = "http://${Constants.HOST}:${Constants.PORT}/${Constants.API_PATH}/${Constants.API_ENCODE}"
        val encodedString: String = client.post(encodingEndpoint) {
            body = state.decodedString
        }

        setState(PageState(state.decodedString, encodedString))
    }

    override fun RBuilder.render() {
        div {
            p { + "Decoded string" }
            textarea {
                attrs {
                    value = state.decodedString
                    onChange = { event ->
                        setState(PageState((event.target as HTMLTextAreaElement).value, state.encodedString))
                    }
                }
            }
            p {
                button(type = ButtonType.button) {
                    + "Encode ⬇️ "
                    attrs {
                        onClick = {
                            scope.launch {
                                onClickEncode()
                            }
                        }
                    }
                }
            }
            p { + "Encoded string" }
            textarea {
                attrs {
                    value = state.encodedString
                    onChange = { event ->
                        setState(PageState(state.decodedString, (event.target as HTMLTextAreaElement).value))
                    }
                }
            }
        }
    }
}
 */
