module Update exposing (..)

import Types exposing (Model, Msg(..), FieldChange(..))
import Material


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        Mdl m ->
            Material.update m model

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
