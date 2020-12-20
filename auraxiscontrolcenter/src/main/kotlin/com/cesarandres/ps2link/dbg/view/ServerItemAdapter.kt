package com.cesarandres.ps2link.dbg.view

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.dbg.util.Logger
import com.cramsan.ps2link.appcore.DBGServiceClient
import com.cramsan.ps2link.appcore.dbg.CensusLang
import com.cramsan.ps2link.appcore.dbg.Namespace
import com.cramsan.ps2link.appcore.dbg.content.World
import com.cramsan.ps2link.appcore.dbg.content.WorldEvent
import com.cramsan.ps2link.appcore.dbg.content.response.server.PS2
import java.util.ArrayList
import java.util.HashMap
import java.util.Locale

class ServerItemAdapter(private val context: Context, serverList: List<World>, val dbgCensus: DBGServiceClient, val namespace: Namespace) : BaseAdapter() {
    private val mInflater: LayoutInflater
    private val serverList: ArrayList<World>
    private val channelMap: HashMap<CompoundButton.OnCheckedChangeListener, String>

    init {
        // Cache the LayoutInflate to avoid asking for a new one each time.
        this.mInflater = LayoutInflater.from(context)
        this.serverList = ArrayList(serverList)
        this.channelMap = HashMap<CompoundButton.OnCheckedChangeListener, String>()
        val settings = PreferenceManager.getDefaultSharedPreferences(context)

        for (world in this.serverList) {
            var channel = namespace.toString() + "-" + world.world_id
            channel = channel.replace(":v2", "")
            world.isRegistered = settings.getBoolean("parse_$channel", false)
            // REMOVE THIS TO ENABLE PUSH NOTIFICATIONS
            world.isRegistered = false
        }
    }

    /**
     * This method will use the LiveServer object to pass population information
     * to the list of servers found in the API
     *
     * @param serverList List of servers with population information
     */
    fun setServerPopulation(serverList: PS2) {
        var name: String?
        var population: String? = ""
        for (world in this.serverList) {
            name = world.name!!.localizedName(CensusLang.EN)
            try {
                if (name == "Briggs") {
                    population = serverList.live!!.briggs!!.status
                } else if (name == "Connery") {
                    population = serverList.live!!.connery!!.status
                } else if (name == "Miller") {
                    population = serverList.live!!.miller!!.status
                } else if (name == "Cobalt") {
                    population = serverList.live!!.cobalt!!.status
                } else if (name == "Ceres") {
                    population = serverList.livePS4!!.ceres!!.status
                } else if (name == "Crux") {
                    population = serverList.livePS4!!.crux!!.status
                } else if (name == "Dahaka") {
                    population = serverList.livePS4!!.dahaka!!.status
                } else if (name == "Genudine") {
                    population = serverList.livePS4!!.genudine!!.status
                } else if (name == "Lithcorp") {
                    population = serverList.livePS4!!.lithcorp!!.status
                } else if (name == "Palos") {
                    population = serverList.livePS4!!.palos!!.status
                } else if (name == "Rashnu") {
                    population = serverList.livePS4!!.rashnu!!.status
                } else if (name == "Searhus") {
                    population = serverList.livePS4!!.searhus!!.status
                } else if (name == "Emerald" || name == "Smaragd" || name == "Esmeralda" || name == "Smeraldo") {
                    population = serverList.live!!.emerald!!.status
                } else {
                    population = null
                }
            } catch (e: NullPointerException) {
                Logger.log(Log.INFO, this, "Population info not available for server " + name!!)
                population = null
            }

            world.population = population
        }

        this.notifyDataSetChanged()
    }

    fun setServerAlert(event: WorldEvent) {
        for (world in this.serverList) {
            if (world.world_id == event.world_id) {
                world.lastAlert = event
            }
        }

        this.notifyDataSetChanged()
    }

    fun onItemSelected(index: Int, context: Context) {
        val world = this.serverList[index]
        var channel = namespace.toString() + "-" +
            this.serverList[index].world_id
        channel = channel.replace(":v2", "")
        if (world.isRegistered) {
            // ParsePush.unsubscribeInBackground(channel);
        } else {
            // ParsePush.subscribeInBackground(channel);
        }
        world.isRegistered = !world.isRegistered

        val settings = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = settings.edit()
        editor.putBoolean("parse_$channel", world.isRegistered)
        editor.commit()

        // REMOVE THIS TO ENABLE PUSH NOTIFICATIONS
        // ParsePush.unsubscribeInBackground(channel);
        // world.setIsRegistered(false);
        // makeText(context, R.string.toast_push_notifications_disabled, LENGTH_LONG).show()

        notifyDataSetInvalidated()
    }

    override fun getCount(): Int {
        return this.serverList.size
    }

    override fun getItem(position: Int): World {
        return this.serverList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        // A ViewHolder keeps references to children views to avoid
        // unneccessary calls
        // to findViewById() on each row.
        val holder: ViewHolder

        // When convertView is not null, we can reuse it directly, there is
        // no need
        // to reinflate it. We only inflate a new View when the convertView
        // supplied
        // by ListView is null.
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_server_item, parent, false)

            // Creates a ViewHolder and store references to the two children
            // views
            // we want to bind data to.
            holder = ViewHolder()
            holder.serverStatus =
                convertView!!.findViewById<View>(R.id.textViewServerStatus) as TextView
            holder.serverName =
                convertView.findViewById<View>(R.id.textViewServerListName) as TextView
            holder.serverRegion =
                convertView.findViewById<View>(R.id.textViewServerListRegion) as TextView
            holder.serverPopulation =
                convertView.findViewById<View>(R.id.textViewServerPopulation) as TextView
            holder.serverAlert =
                convertView.findViewById<View>(R.id.textViewServerAlert) as TextView
            holder.serverAlertCheckBox =
                convertView.findViewById<View>(R.id.checkBoxServerAlert) as CheckBox
            convertView.tag = holder
        } else {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            holder = convertView.tag as ViewHolder
        }

        // Bind the data efficiently with the holder.
        val serverPopulation = this.serverList[position].population
        if (serverPopulation != null) {
            if (serverPopulation.equals("low", ignoreCase = true)) {
                // Orange color
                holder.serverPopulation!!.setTextColor(Color.rgb(250, 140, 0))
                holder.serverPopulation!!.text = context.resources.getString(R.string.text_server_population) + " " + context.resources.getString(R.string.text_low).toUpperCase()
            } else if (serverPopulation.equals("medium", ignoreCase = true)) {
                holder.serverPopulation!!.setTextColor(Color.YELLOW)
                holder.serverPopulation!!.text =
                    context.resources.getString(R.string.text_server_population) + " " + context.resources.getString(R.string.text_medium).toUpperCase()
            } else if (serverPopulation.equals("high", ignoreCase = true)) {
                holder.serverPopulation!!.setTextColor(Color.GREEN)
                holder.serverPopulation!!.text =
                    context.resources.getString(R.string.text_server_population) + " " + context.resources.getString(R.string.text_high).toUpperCase()
            } else {
                holder.serverPopulation!!.setTextColor(Color.RED)
                holder.serverPopulation!!.text =
                    context.resources.getString(R.string.text_server_population) + " " + serverPopulation.toUpperCase()
            }
        } else {
            holder.serverPopulation!!.text =
                context.resources.getString(R.string.text_server_population) + " " + "Not Available"
            holder.serverPopulation!!.setTextColor(Color.WHITE)
        }

        val serverState = this.serverList[position].state
        if (serverState != null) {
            holder.serverStatus!!.text = serverState.toUpperCase(Locale.getDefault())
            if (serverState.equals("online", ignoreCase = true)) {
                holder.serverStatus!!.setTextColor(Color.GREEN)
            } else {
                holder.serverStatus!!.setTextColor(Color.RED)
            }
        } else {
            holder.serverStatus!!.text = "UNKNOWN"
            holder.serverStatus!!.setTextColor(Color.RED)
        }

        holder.serverAlertCheckBox!!.isChecked = this.serverList[position].isRegistered
        holder.serverAlertCheckBox!!.visibility = View.GONE

        val name = this.serverList[position].name!!.localizedName(CensusLang.EN)

        if (name == "Briggs") {
            holder.serverRegion!!.text = "(AU)"
        } else if (name == "Emerald" || name == "Smaragd" || name == "Esmeralda" || name == "Smeraldo") {
            holder.serverRegion!!.text = "(US EAST)"
        } else if (name == "Connery") {
            holder.serverRegion!!.text = "(US WEST)"
        } else if (name == "Miller" || name == "Cobalt") {
            holder.serverRegion!!.text = "(EU)"
        } else if (name == "Jaeger") {
            holder.serverRegion!!.text = "( - )"
        } else {
            holder.serverRegion!!.text = ""
        }

        holder.serverName!!.text = name

        val lastAlert = getItem(position).lastAlert
        if (lastAlert != null) {
            try {
                // THIS ARE GREEN
                //   metagame_event_state_id: "135",
                //            name: "started"
                //    metagame_event_state_id: "136",
                //            name: "restarted"
                //    metagame_event_state_id: "139",
                //            name: "xp bonus changed"
                // THIS ARE GRAY
                //    metagame_event_state_id: "137",
                //            name: "canceled"
                //    metagame_event_state_id: "138",
                //            name: "ended"

                if (lastAlert.metagame_event_state == "135" ||
                    lastAlert.metagame_event_state == "136" ||
                    lastAlert.metagame_event_state == "139"
                ) {
                    holder.serverAlert!!.text =
                        (
                            context.resources.getString(R.string.text_server_alert_current) +
                                " " + lastAlert.metagame_event_id_join_metagame_event!!.description!!.localizedName(CensusLang.EN)
                            )

                    holder.serverAlert!!.setTextColor(Color.argb(255, 120, 235, 235))
                    holder.serverAlert!!.setTypeface(null, Typeface.BOLD)
                } else {
                    holder.serverAlert!!.text =
                        (
                            context.resources.getString(R.string.text_server_alert_recently) +
                                " " + lastAlert.metagame_event_id_join_metagame_event!!.description!!.localizedName(CensusLang.EN)
                            )
                    holder.serverAlert!!.setTypeface(null, Typeface.NORMAL)
                    holder.serverAlert!!.setTextColor(Color.GRAY)
                }
            } catch (e: NullPointerException) {
                holder.serverAlert!!.text =
                    (
                        context.resources.getString(R.string.text_server_alert_recently) +
                            " " + context.resources.getString(R.string.text_unknown).toUpperCase()
                        )
                holder.serverAlert!!.setTypeface(null, Typeface.NORMAL)
            }
        } else {
            holder.serverAlert!!.text =
                (
                    context.resources.getString(R.string.text_server_alert_recently) +
                        " " + context.resources.getString(R.string.text_none)
                    )
            holder.serverAlert!!.setTypeface(null, Typeface.NORMAL)
        }
        holder.serverAlert!!.visibility = View.GONE

        return convertView
    }

    internal class ViewHolder {

        var serverStatus: TextView? = null
        var serverName: TextView? = null
        var serverRegion: TextView? = null
        var serverPopulation: TextView? = null
        var serverAlert: TextView? = null
        var serverAlertCheckBox: CheckBox? = null
    }
}
