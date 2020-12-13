package com.cesarandres.ps2link.dbg.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.cesarandres.ps2link.R
import com.cramsan.ps2link.appcore.dbg.content.character.Stat

class StatItemAdapter(
    private val context: Context,
    private val stats: List<Stat>,
    characterId: String
) : BaseAdapter() {

    protected var mInflater: LayoutInflater

    init {
        this.mInflater = LayoutInflater.from(context)
        var kills: Stat? = null
        var deaths: Stat? = null
        var score: Stat? = null
        var time: Stat? = null
        for (stat in stats) {
            if (stat.stat_name == "kills") {
                kills = stat
            } else if (stat.stat_name == "deaths") {
                deaths = stat
            } else if (stat.stat_name == "score") {
                score = stat
            } else if (stat.stat_name == "time") {
                time = stat
            }
        }
        val kdr = Stat()
        /*
        if (deaths!!.all_time == "0") {
            deaths.all_time = "1"
        }
        if (deaths.today() == 0f) {
            deaths.today = 1F
        }
        if (deaths.thisWeek() == 0f) {
            deaths.thisWeek = 1F
        }
        if (deaths.thisMonth() == 0f) {
            deaths.thisWeek = 1F
        }
        kdr.day = kdr.Day()
        kdr.week = kdr.Week()
        kdr.month = kdr.Month()

        kdr.stat_name = "kdr"
        kdr.all_time = java.lang.Float.toString(
            java.lang.Float.parseFloat(kills!!.all_time!!) / java.lang.Float.parseFloat(deaths.all_time!!)
        )
        kdr.today = kills.today / deaths.today
        kdr.thisWeek = kills.thisWeek / deaths.thisWeek
        kdr.thisMonth = kills.thisMonth / deaths.thisMonth
        this.stats.add(0, kdr)

        val sph = Stat()
        if (time!!.all_time == "0") {
            time.all_time = "1"
        }
        if (time.today == 0f) {
            time.today = 1F
        }
        if (time.thisWeek == 0f) {
            time.thisWeek = 1F
        }
        if (time.thisMonth == 0f) {
            time.thisWeek = 1F
        }
        sph.day = sph.Day()
        sph.week = sph.Week()
        sph.month = sph.Month()

        sph.stat_name = "scorehour"
        sph.all_time = java.lang.Float.toString(
            java.lang.Float.parseFloat(score!!.all_time!!) / (java.lang.Float.parseFloat(time.all_time!!) / 3600f)
        )
        sph.today = score.today / (time.today / 3600f)
        sph.thisWeek = score.thisWeek / (time.thisWeek / 3600f)
        sph.thisMonth = score.thisMonth / (time.thisMonth / 3600f)
        this.stats.add(0, sph)
        */
    }

    override fun getCount(): Int {
        return this.stats.size
    }

    override fun getItem(position: Int): Stat {
        return stats[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val holder: ViewHolder

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_stat_item, parent, false)

            holder = ViewHolder()
            holder.name = convertView!!.findViewById<View>(R.id.TextViewStatItemName) as TextView
            holder.total = convertView.findViewById<View>(R.id.TextViewStatItemTotal) as TextView
            holder.today = convertView.findViewById<View>(R.id.TextViewStatItemToday) as TextView
            holder.week = convertView.findViewById<View>(R.id.TextViewStatItemWeek) as TextView
            holder.month = convertView.findViewById<View>(R.id.TextViewStatItemMonth) as TextView
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }

        val stat = getItem(position)
        if (stat.stat_name == "time") {
            holder.name!!.setText(R.string.text_time_played_caps)
            val hours = context.resources.getString(R.string.text_hours)
            holder.total!!.text =
                context.resources.getString(R.string.text_stat_all) + " " + java.lang.Float.valueOf(stat.all_time!!).toInt() / 3600 + " " + hours
            holder.today!!.text =
                context.resources.getString(R.string.text_stat_today) + " " + java.lang.Float.valueOf(stat.day!!.d01!!).toInt() / 3600 + " " + hours
            holder.week!!.text =
                context.resources.getString(R.string.text_stat_week) + " " + java.lang.Float.valueOf(stat.week!!.w01!!).toInt() / 3600 + " " + hours
            holder.month!!.text =
                context.resources.getString(R.string.text_stat_month) + " " + java.lang.Float.valueOf(stat.month!!.m01!!).toInt() / 3600 + " " + hours
        } else {
            var statName = ""

            if (stat.stat_name!!.equals("battle_rank", ignoreCase = true)) {
                statName = context.resources.getString(R.string.text_stat_battle_rank)
            } else if (stat.stat_name!!.equals("certs", ignoreCase = true)) {
                statName = context.resources.getString(R.string.text_stat_certs)
            } else if (stat.stat_name!!.equals("deaths", ignoreCase = true)) {
                statName = context.resources.getString(R.string.text_stat_deaths)
            } else if (stat.stat_name!!.equals("facility_capture", ignoreCase = true)) {
                statName = context.resources.getString(R.string.text_stat_fac_captured)
            } else if (stat.stat_name!!.equals("facility_defend", ignoreCase = true)) {
                statName = context.resources.getString(R.string.text_stat_fac_defended)
            } else if (stat.stat_name!!.equals("kills", ignoreCase = true)) {
                statName = context.resources.getString(R.string.text_stat_kills)
            } else if (stat.stat_name!!.equals("medals", ignoreCase = true)) {
                statName = context.resources.getString(R.string.text_stat_medals)
            } else if (stat.stat_name!!.equals("ribbons", ignoreCase = true)) {
                statName = context.resources.getString(R.string.text_stat_ribbons)
            } else if (stat.stat_name!!.equals("score", ignoreCase = true)) {
                statName = context.resources.getString(R.string.text_stat_score)
            } else if (stat.stat_name!!.equals("kdr", ignoreCase = true)) {
                statName = context.resources.getString(R.string.text_stat_kdr)
            } else if (stat.stat_name!!.equals("scorehour", ignoreCase = true)) {
                statName = context.resources.getString(R.string.text_stat_score_hour)
            }

            holder.name!!.text = statName
            holder.total!!.text =
                context.resources.getString(R.string.text_stat_all) + " " + stat.all_time
            holder.today!!.text =
                context.resources.getString(R.string.text_stat_today) + " " + stat.day!!.d01
            holder.week!!.text =
                context.resources.getString(R.string.text_stat_week) + " " + stat.week!!.w01
            holder.month!!.text =
                context.resources.getString(R.string.text_stat_month) + " " + stat.month!!.m01
        }

        return convertView
    }

    internal class ViewHolder {
        var name: TextView? = null
        var total: TextView? = null
        var today: TextView? = null
        var week: TextView? = null
        var month: TextView? = null
    }
}
