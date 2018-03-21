package nor.example.leica

import griffon.core.Configuration
import griffon.core.artifact.GriffonView
import griffon.inject.MVCMember
import griffon.metadata.ArtifactProviderFor
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView
import javax.annotation.Nonnull

@ArtifactProviderFor(GriffonView)
class DistoLeicaView extends AbstractDistoLeicaView {

    @MVCMember
    @Nonnull
    DistoLeicaController controller

    @MVCMember
    @Nonnull
    DistoLeicaModel model

    void initUI() {
        Configuration conf = application.configuration


        builder.application(title: conf.getAsString("application.title"),
                sizeToScene: true,
                centerOnScreen: true,
                minWidth: conf.getAsInt("application.initialSize.width"),
                minHeight: conf.getAsInt("application.initialSize.height"),
                icons: [getImage("griffon-icon-256x256.png").image],
                name: 'mainWindow') {
            scene(fill: WHITE,
                    width: conf.getAsInt("application.initialSize.width"),
                    height: conf.getAsInt("application.initialSize.height")) {
                g = gridPane(maxHeight: Double.MAX_VALUE, maxWidth: Double.MAX_VALUE,
                        vgap: 5,
                        padding: 5) {

                    createSampleForm(0, 0)

                }
                connectActions(g, controller)
                connectMessageSource(g)
            }
        }
    }

    private createSampleForm(row, column) {
        builder.gridPane(row: row, column: column,
                maxHeight: Double.MAX_VALUE, maxWidth: Double.MAX_VALUE,
                padding: formPanePadding, hgap: 5, vgap: 5,
                style: formBackgroundStyle,
                valignment: "center") {
            columnConstraints hgrow: "never"
            columnConstraints hgrow: "always"
            columnConstraints hgrow: "never"
            columnConstraints hgrow: "never"
            rowConstraints vgrow: "never"
            rowConstraints vgrow: "never"
            rowConstraints vgrow: "never"
            rowConstraints vgrow: "always"

            label(column: 0, row: 0,
                    columnSpan: 3,
                    margin: formItemMargin,
                    maxWidth: Double.MAX_VALUE,
                    font: titleLabelFont,
                    alignment: "center",
                    text: getMessage("view.distoleica.sampleform.info.label"))

            label(column: 0, row: 1,
                    margin: formItemMargin,
                    text: getMessage("view.distoleica.sampleform.address.label"))

            textField(column: 1, row: 1,
                    margin: formItemMargin,
                    text: bind(model.sampleForm.address()))

            button(column: 2, row: 1,
                    margin: formItemMargin,
                    text: getMessage("view.distoleica.sampleform.connect.button.caption"),
                    tooltip: getToolTip("view.distoleica.sampleform.connect.button.tooltip"),
                    onAction: { controller.toogleAcquireConnection() })

            label(column: 0, row: 2,
                    margin: formItemMargin,
                    text: getMessage("view.distoleica.sampleform.command.label"))

            textField(column: 1, row: 2,
                    margin: formItemMargin,
                    text: bind(model.sampleForm.command()))

            button(column: 2, row: 2,
                    margin: formItemMargin,
                    text: getMessage("view.distoleica.sampleform.command.button.caption"),
                    tooltip: getToolTip("view.distoleica.sampleform.command.button.tooltip"),
                    onAction: { controller.toogleAcquireCommand() })

            label(column: 0, row: 3,
                    margin: formItemMargin,
                    text: getMessage("view.distoleica.sampleform.output.label"))

            textArea(id: 'comboBoxSampleInfoFormNote',
                    column: 1, row: 3,
                    columnSpan: 2,
                    margin: formItemMargin,
                    text: bind(model.sampleForm.output()),
            )
        }
    }
}