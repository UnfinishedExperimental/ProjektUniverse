/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.client.rendering.geometrie.unpacked;

import java.util.Arrays;

/**
 * H�lt alle Werte eines in einem MTL File definierten Materials
 * @author Daniel Heinrich
 */
public class Material
{
    /**
     * Ns exponent

     * Specifies the specular exponent for the current material.  This defines 
     * the focus of the specular highlight.

     * "exponent" is the value for the specular exponent.  A high exponent 
     * results in a tight, concentrated highlight.  Ns values normally range 
     * from 0 to 1000.
     */
    private float specular_exponent = 20; //Ns
    /**
     * Ni optical_density

     * Specifies the optical density for the surface.  This is also known as
     * index of refraction.

     * "optical_density" is the value for the optical density.  The values can
     * range from 0.001 to 10.  A value of 1.0 means that light does not bend
     * as it passes through an object.  Increasing the optical_density
     * increases the amount of bending.  Glass has an index of refraction of
     * about 1.5.  Values of less than 1.0 produce bizarre results and are not
     * recommended.
     */
    private float optical_density; //Ni
    /**
     * sharpness value

     * Specifies the sharpness of the reflections from the local reflection
     * map.  If a material does not have a local reflection map defined in its
     * material definition, sharpness will apply to the global reflection map
     * defined in PreView.

     * "value" can be a number from 0 to 1000.  The default is 60.  A high
     * value results in a clear reflection of objects in the reflection map.

     * Tip	Sharpness values greater than 100 map introduce aliasing effects
     * in flat surfaces that are viewed at a sharp angle
     */
    private float sharpness; //sharpness
    /**
     * d -halo factor

     * Specifies that a dissolve is dependent on the surface orientation
     * relative to the viewer.  For example, a sphere with the following
     * dissolve, d -halo 0.0, will be fully dissolved at its center and will
     * appear gradually more opaque toward its edge.

     * "factor" is the minimum amount of dissolve applied to the material.
     * The amount of dissolve will vary between 1.0 (fully opaque) and the
     * specified "factor".  The formula is:

     * dissolve = 1.0 - (N*v)(1.0-factor)

     * For a definition of terms, see "Illumination models" on page 5-30.
     */
    private float alpha; //d
    /**
     * illum:
     * 0		Color on and Ambient off
     * 1		Color on and Ambient on
     * 2		Highlight on
     * 3		Reflection on and Ray trace on
     * 4		Transparency: Glass on
     *                      Reflection: Ray trace on
     * 5		Reflection: Fresnel on and Ray trace on
     * 6		Transparency: Refraction on
     *                      Reflection: Fresnel off and Ray trace on
     * 7		Transparency: Refraction on
     *                      Reflection: Fresnel on and Ray trace on
     * 8		Reflection on and Ray trace off
     * 9		Transparency: Glass on
     *                      Reflection: Ray trace off
     * 10		Casts shadows onto invisible surfaces
     */
    private int illum;
    private float[] ambient = new float[4];//ka
    private float[] diffuse = new float[4];//kd
    private float[] sepcular = new float[4];//ks
    private float[] emission = new float[4];//ke
    private String ambientTex;  // map_Ka
    private String diffuseTex;  //map_Kd
    private String specularTex; //map_Ks
    private String normalTex;   //map_bump
    private String alphaTex;   //map_d
    private String bumpTex;     //bump
    private String name;

    public Material(String name) {
        this.name = name;
    }

    public Material(float[] diffuse, float[] ambient, float[] emission,
                    float[] specular,
                    float optical_density, float sharpness, float alpha,
                    int illum, String ambientTex, String diffuseTex,
                    String specularTex, String normalTex, String alphaTex,
                    String bumpTex, String name) {
        setDiffuse(diffuse);
        setAmbient(ambient);
        setEmission(emission);
        setSepcular(sepcular);
        this.optical_density = optical_density;
        this.sharpness = sharpness;
        this.alpha = alpha;
        this.illum = illum;
        this.ambientTex = ambientTex;
        this.diffuseTex = diffuseTex;
        this.specularTex = specularTex;
        this.normalTex = normalTex;
        this.alphaTex = alphaTex;
        this.bumpTex = bumpTex;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public String getAlphaTex() {
        return alphaTex;
    }

    public void setAlphaTex(String alphaTex) {
        this.alphaTex = alphaTex;
    }

    public float[] getAmbient() {
        return ambient;
    }

    public void setAmbient(float[] ambient) {
        int min = Math.min(4, ambient.length);
        int i;
        for (i = 0; i < min; ++i)
            this.ambient[i] = ambient[i];
        while(i < 4)
            this.ambient[i++] = 1;
    }

    public String getAmbientTex() {
        return ambientTex;
    }

    public void setAmbientTex(String ambientTex) {
        this.ambientTex = ambientTex;
    }

    public String getBumbTex() {
        return bumpTex;
    }

    public void setBumbTex(String bumbTex) {
        this.bumpTex = bumbTex;
    }

    public float[] getDiffuse() {
        return diffuse;
    }

    public void setDiffuse(float[] diffuse) {
        int min = Math.min(4, diffuse.length);
        int i;
        for (i = 0; i < min; ++i)
            this.diffuse[i] = diffuse[i];
        while(i < 4)
            this.diffuse[i++] = 1;
    }

    public String getDiffuseTex() {
        return diffuseTex;
    }

    public void setDiffuseTex(String diffuseTex) {
        this.diffuseTex = diffuseTex;
    }

    public float[] getEmission() {
        return emission;
    }

    public void setEmission(float[] emission) {
        int min = Math.min(4, emission.length);
        int i;
        for (i = 0; i < min; ++i)
            this.emission[i] = emission[i];
        while(i < 4)
            this.emission[i++] = 1;
    }

    public int getIllum() {
        return illum;
    }

    public void setIllum(int illum) {
        this.illum = illum;
    }

    public String getNormalTex() {
        return normalTex;
    }

    public void setNormalTex(String normalTex) {
        this.normalTex = normalTex;
    }

    public float getOptical_density() {
        return optical_density;
    }

    public void setOptical_density(float optical_density) {
        this.optical_density = optical_density;
    }

    public float[] getSepcular() {
        return sepcular;
    }

    public void setSepcular(float[] sepcular) {
        int min = Math.min(4, sepcular.length);
        int i;
        for (i = 0; i < min; ++i)
            this.sepcular[i] = sepcular[i];
        while(i < 4)
            this.sepcular[i++] = 1;
    }

    public float getSharpness() {
        return sharpness;
    }

    public void setSharpness(float sharpness) {
        this.sharpness = sharpness;
    }

    public String getSpecularTex() {
        return specularTex;
    }

    public void setSpecularTex(String specularTex) {
        this.specularTex = specularTex;
    }

    public float getSpecular_exponent() {
        return specular_exponent;
    }

    public void setSpecular_exponent(float specular_exponent) {
        this.specular_exponent = specular_exponent;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public Material clone() {
        return new Material(diffuse.clone(), ambient.clone(), emission.clone(), sepcular.clone(),
                            optical_density, sharpness, alpha, illum, ambientTex,
                            diffuseTex, specularTex, normalTex, alphaTex,
                            bumpTex, name);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Material other = (Material) obj;
        if (this.specular_exponent != other.specular_exponent)
            return false;
        if (this.optical_density != other.optical_density)
            return false;
        if (this.sharpness != other.sharpness)
            return false;
        if (this.alpha != other.alpha)
            return false;
        if (this.illum != other.illum)
            return false;
        if (!Arrays.equals(this.ambient, other.ambient))
            return false;
        if (!Arrays.equals(this.diffuse, other.diffuse))
            return false;
        if (!Arrays.equals(this.sepcular, other.sepcular))
            return false;
        if (!Arrays.equals(this.emission, other.emission))
            return false;
        if ((this.ambientTex == null) ? (other.ambientTex != null) : !this.ambientTex.
                equals(other.ambientTex))
            return false;
        if ((this.diffuseTex == null) ? (other.diffuseTex != null) : !this.diffuseTex.
                equals(other.diffuseTex))
            return false;
        if ((this.specularTex == null) ? (other.specularTex != null) : !this.specularTex.
                equals(other.specularTex))
            return false;
        if ((this.normalTex == null) ? (other.normalTex != null) : !this.normalTex.
                equals(other.normalTex))
            return false;
        if ((this.alphaTex == null) ? (other.alphaTex != null) : !this.alphaTex.
                equals(other.alphaTex))
            return false;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(
                other.name))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Float.floatToIntBits(this.specular_exponent);
        hash = 29 * hash + Float.floatToIntBits(this.optical_density);
        hash = 29 * hash + Float.floatToIntBits(this.sharpness);
        hash = 29 * hash + Float.floatToIntBits(this.alpha);
        hash = 29 * hash + this.illum;
        hash = 29 * hash + Arrays.hashCode(this.ambient);
        hash = 29 * hash + Arrays.hashCode(this.diffuse);
        hash = 29 * hash + Arrays.hashCode(this.sepcular);
        hash = 29 * hash + Arrays.hashCode(this.emission);
        hash =
        29 * hash + (this.ambientTex != null ? this.ambientTex.hashCode() : 0);
        hash =
        29 * hash + (this.diffuseTex != null ? this.diffuseTex.hashCode() : 0);
        hash =
        29 * hash + (this.specularTex != null ? this.specularTex.hashCode() : 0);
        hash =
        29 * hash + (this.normalTex != null ? this.normalTex.hashCode() : 0);
        hash =
        29 * hash + (this.alphaTex != null ? this.alphaTex.hashCode() : 0);
        hash = 29 * hash + (this.bumpTex != null ? this.bumpTex.hashCode() : 0);
        hash = 29 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }
}
