package nor.example.leica

import griffon.core.artifact.GriffonModel
import griffon.transform.FXObservable
import griffon.metadata.ArtifactProviderFor
import groovyx.javafx.beans.FXBindable

@ArtifactProviderFor(GriffonModel)
class DistoLeicaModel {

    class SampleForm {
        private SampleForm() {

        }

        @FXBindable
        String address = ""

        @FXBindable
        String command = ""

        @FXBindable
        String output = ""
    }

    def sampleForm = new SampleForm()

}