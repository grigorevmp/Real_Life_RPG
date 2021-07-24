package com.nonameteam.realliferpg

import android.R.attr.theme
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.oscim.android.MapView
import org.oscim.backend.CanvasAdapter
import org.oscim.core.MapPosition
import org.oscim.layers.tile.buildings.BuildingLayer
import org.oscim.layers.tile.vector.VectorTileLayer
import org.oscim.layers.tile.vector.labeling.LabelLayer
import org.oscim.renderer.GLViewport
import org.oscim.scalebar.DefaultMapScaleBar
import org.oscim.scalebar.MapScaleBar
import org.oscim.scalebar.MapScaleBarLayer
import org.oscim.theme.IRenderTheme
import org.oscim.tiling.source.mapfile.MapFileTileSource
import org.oscim.theme.VtmThemes
import java.io.FileInputStream

class MapFragment(): Fragment() {
    private val SELECT_MAP_FILE = 0;
    private var mapView: MapView? = null
    private var theme: IRenderTheme? = null
    private var container: ViewGroup? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.container = container
        mapView = MapView(container?.context)

        // Open map
        val intent =
            Intent(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) Intent.ACTION_OPEN_DOCUMENT else Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*"
        startActivityForResult(intent, SELECT_MAP_FILE)

        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == SELECT_MAP_FILE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val uri = data.data
                openMap(uri!!)
            }
        }
    }

    private fun openMap(uri: Uri){
        try {
            // Tile source
            val tileSource = MapFileTileSource()
            val fis: FileInputStream = container?.context?.contentResolver?.openInputStream(uri) as FileInputStream
            tileSource.setMapFileInputStream(fis)

            // Vector layer
            val tileLayer = mapView!!.map().setBaseMap(tileSource)

            // Building layer
            mapView!!.map().layers().add(BuildingLayer(mapView!!.map(), tileLayer))

            // Label layer
            mapView!!.map().layers().add(LabelLayer(mapView!!.map(), tileLayer))

            // Render theme
            theme = mapView!!.map().setTheme(VtmThemes.DEFAULT)

            // Scale bar
            val mapScaleBar: MapScaleBar = DefaultMapScaleBar(mapView!!.map())
            val mapScaleBarLayer = MapScaleBarLayer(mapView!!.map(), mapScaleBar)
            mapScaleBarLayer.renderer.setPosition(GLViewport.Position.BOTTOM_LEFT)
            mapScaleBarLayer.renderer.setOffset(5 * CanvasAdapter.getScale(), 0f)
            mapView!!.map().layers().add(mapScaleBarLayer)

            // Note: this map position is specific to Berlin area
            mapView!!.map().setMapPosition(52.517037, 13.38886, (1 shl 12).toDouble())
        } catch (e: Exception) {
            /*
             * In case of map file errors avoid crash, but developers should handle these cases!
             */
            e.printStackTrace()
        }
    }
}
