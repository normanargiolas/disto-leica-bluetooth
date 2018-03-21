package nor.example.leica

import griffon.inject.MVCMember
import nor.example.leica.utility.ImageUtilityTrait
import org.apache.commons.lang.StringEscapeUtils
import javafx.scene.control.Tooltip
import javafx.scene.text.Font
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView

import javax.annotation.Nonnull

/**
 * Created by sardylan on 28/03/17.
 */
abstract class AbstractDistoLeicaView extends AbstractJavaFXGriffonView implements ImageUtilityTrait {

    @MVCMember
    @Nonnull
    protected FactoryBuilderSupport builder

    protected static rowConstraintsPercentHeight = 14
    protected static formPanePadding = [5, 5, 10, 5]
    protected static formItemMargin = [0, 5, 0, 5]
    protected static titleLabelFont = new Font("Arial", 18)

    protected static formBackgroundStyle =
            "-fx-background-color: linear-gradient(#EEEEEE 0%, #F8F8F8 100%);" +
                    "-fx-background-insets: 1;" +
                    "-fx-background-radius: 15;"

    protected String getMessage(String key) {
        StringEscapeUtils.unescapeHtml(application.messageSource.getMessage(key, ""))
    }

    @Override
    InputStream getInputStream(String imagePath) {
        return null
    }

    protected Tooltip getToolTip(String messageKey) {
        new Tooltip(getMessage(messageKey))
    }

}
