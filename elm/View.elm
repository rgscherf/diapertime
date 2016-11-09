module View exposing (..)

import Html exposing (..)
import Types exposing (Model, Msg)


view : Model -> Html Msg
view model =
    div [] [ text <| toString <| model ]
