package nor.example.leica

import griffon.inject.MVCMember
import nor.example.leica.notification.LeicaNotification
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController

import javax.annotation.Nonnull
import javax.inject.Inject

/**
 * Created by NormaN on 22/03/18.
 *
 **/


abstract class AbstractController extends AbstractGriffonController implements LeicaNotification{

    public static final String EVENT_CONNECTED = "event_connected"
    public static final String EVENT_DISCONNECTED = "event_disconnected"

    @MVCMember
    @Nonnull
    protected DistoLeicaView view

    @MVCMember
    @Nonnull
    protected DistoLeicaModel model

}
