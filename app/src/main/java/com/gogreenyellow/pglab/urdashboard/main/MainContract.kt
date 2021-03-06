package com.gogreenyellow.pglab.urdashboard.main

import com.gogreenyellow.pglab.urdashboard.BasePresenter
import com.gogreenyellow.pglab.urdashboard.BaseView
import com.gogreenyellow.pglab.urdashboard.model.AssignedSubmission
import com.gogreenyellow.pglab.urdashboard.model.QueuedProject
import com.gogreenyellow.pglab.urdashboard.model.SubmissionRequest

/**
 * Created by Paulina on 2018-02-13.
 */
interface MainContract {

    interface Presenter : BasePresenter {
        fun refreshAll(token: String?, force: Boolean)
    }

    interface View : BaseView<Presenter> {
        fun displaySubmissionRequests(submissionRequests: List<SubmissionRequest>)
        fun displayAssignedSubmissions(assignedSubmissions: List<AssignedSubmission>)
        fun displayProjectsQueuedFor(queuedProjects: List<QueuedProject>)
        fun hideRefreshing()
        fun displayTokenData(expiresIn: Long, expirationDate: String)
        fun showTokenDialog(cancelable: Boolean)
    }

}