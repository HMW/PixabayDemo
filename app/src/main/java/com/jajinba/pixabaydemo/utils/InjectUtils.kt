package com.jajinba.pixabaydemo.utils

import com.jajinba.pixabaydemo.model.ImageSearchDataSourceImpl
import com.jajinba.pixabaydemo.model.ImagesSearchDataSource

object InjectUtils {

  fun providerImagesDataSource(): ImagesSearchDataSource = ImageSearchDataSourceImpl()

}
