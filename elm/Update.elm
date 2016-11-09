module Update exposing (..)

import Types exposing (Model, Msg(..), FieldChange(..))


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        NoOp ->
            model ! []

        Entry fc ->
            case fc of
                ChangeAttended sp ->
                    model ! []

                ChangeSkippedPrevious sp ->
                    model ! []

                ChangePoop sp ->
                    model ! []

                ChangePee sp ->
                    model ! []

                ChangeBreastFeed sp ->
                    model ! []

                ChangeBottleFeed sp ->
                    model ! []

                ChangeSlept sp ->
                    model ! []
