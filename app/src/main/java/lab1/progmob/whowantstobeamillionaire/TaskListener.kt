package lab1.progmob.whowantstobeamillionaire

import lab1.progmob.whowantstobeamillionaire.model.Question

interface TaskListener {

    fun onCompleted(questions: MutableList<Question>)
}
