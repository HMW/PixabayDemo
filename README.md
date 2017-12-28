# PixabayDemo
Pixabay demo app, support device using api 21 and above

# How to build this project
1. add `keystore.properties` to project root
2. add your Pixabay api key to `keystore.properties` with following format
    - `PixabayApiKey=xxxxxxxxxxxxxxxxxxxxx`
    
# What I have done
1. MVP pattern
    - list and grid page share same Presenter due to have same logic
2. Load image with [Glide]
3. Handle api request with [Retrofit]
4. Parse JSONObject with [Gson]

# TODOs
1. search history
    - I have already cached search history in `ImageManager`, but have not implement UI
2. infinite scroll on image list
3. download image when long click on item
4. show more info of image (e.g., likes and comments)
5. suggest images user may like (like pinterest)
6. cache image instead of image url


[//]: # "references"
[Glide]: https://github.com/bumptech/glide
[Retrofit]: http://square.github.io/retrofit/
[Gson]: https://github.com/google/gson
