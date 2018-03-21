package nor.example.leica.utility

import javafx.scene.image.Image
import javafx.scene.image.ImageView

/**
 * Created by NormaN on 21/03/18.
 *
 **/


trait ImageUtilityTrait {

    ImageView getIconImage(String name) {
        getImage("icons/${name}.png")
    }

    ImageView getImage(String imagePath) {
        def inputStream = getInputStream(imagePath)
        def image = new Image(inputStream)
        def imageView = new ImageView(image)
        return imageView
    }

    abstract InputStream getInputStream(String imagePath)
}
