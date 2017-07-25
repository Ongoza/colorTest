importPackage(org.gearvrf)
importPackage(org.gearvrf.scene_objects)
importPackage(org.gearvrf.script)

function onInit(gvrf) {
  //var mainScene = gvrf.getNextMainScene();
  Log.d(TAG,"hello from script");

}

function onStep() {
Log.d(TAG,"hello from script on step");
//    if (localAnimation >= 0) {
//        var scale = 1 + .175 * Math.sin((60 - localAnimation) / 60 * 2 * Math.PI);
//        scale *= 50;
//        sceneObject.getTransform().setScale(scale, scale, scale);
//        localAnimation--;
//    }
}