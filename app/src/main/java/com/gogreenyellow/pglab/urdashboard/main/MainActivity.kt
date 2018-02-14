package com.gogreenyellow.pglab.urdashboard.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import com.gogreenyellow.pglab.urdashboard.R
import com.gogreenyellow.pglab.urdashboard.model.AssignedSubmission
import com.gogreenyellow.pglab.urdashboard.model.QueuedProject
import com.gogreenyellow.pglab.urdashboard.model.SubmissionRequest
import com.gogreenyellow.pglab.urdashboard.settings.SettingsActivity
import com.gogreenyellow.pglab.urdashboard.util.DateUtil
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.sheet_request_settings.*
import kotlinx.android.synthetic.main.sheet_token.*
import kotlinx.android.synthetic.main.srs_project_item.view.*

class MainActivity : AppCompatActivity(), MainContract.View {

    override lateinit var presenter: MainContract.Presenter

    companion object {
        const val UDACITY_DASHBOARD_URL = "https://mentor-dashboard.udacity.com/reviews/overview"
        const val UPDATE_TOKEN_DIALOG_TAG = "update_token_dialog"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = MainPresenter(this)

        am_swipe_refresh_layout.setColorSchemeColors(resources.getColor(R.color.colorAccent))
        am_swipe_refresh_layout.setOnRefreshListener({
            presenter.refreshAll()
        })

        st_update_button.setOnClickListener { showUpdateTokenDialog() }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.mm_open_udacity_website -> {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(UDACITY_DASHBOARD_URL))
                startActivity(intent)
            }
            R.id.mm_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun displaySubmissionRequests(submissionRequests: List<SubmissionRequest>) {
        if (submissionRequests.isNotEmpty()) {
            val first = submissionRequests.get(0)
            srs_queue_left_time.text = DateUtil.getTimeLeft(first.closedAt, this)
            srs_queue_end_time.text = DateUtil.getTime(first.closedAt)
        }
    }

    override fun displayAssignedSubmissions(assignedSubmissions: List<AssignedSubmission>) {

    }

    override fun displayProjectsQueuedFor(queuedFor: List<QueuedProject>) {
        srs_projects_list_container.removeAllViews()
        for (project in queuedFor) {
            val item = LayoutInflater.from(this).inflate(R.layout.srs_project_item,
                    srs_projects_list_container, false)

            item.pi_id_view.text = project.projectId
            item.pi_project_name.text = project.projectName
            item.pi_project_price.text = project.projectPrice
            if (project.queuedFor) {
                item.pi_project_queue_state.setImageResource(R.drawable.ic_toggle_switch)
            } else {
                item.pi_project_queue_state.setImageResource(R.drawable.ic_toggle_switch_off)
            }
            srs_projects_list_container.addView(item)
        }
    }

    fun showUpdateTokenDialog() {
        UpdateTokenDialog().show(supportFragmentManager, UPDATE_TOKEN_DIALOG_TAG)
    }

}
