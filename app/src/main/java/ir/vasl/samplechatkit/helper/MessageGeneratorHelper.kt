package ir.vasl.samplechatkit.helper

import ir.vasl.chatkitlight.model.ConversationModel
import ir.vasl.chatkitlight.utils.globalEnums.ConversationStatus
import ir.vasl.chatkitlight.utils.globalEnums.ConversationType
import ir.vasl.chatkitlight.utils.globalEnums.FileType
import ir.vasl.samplechatkit.helper.IdGeneratorHelper.Companion.getIncreasingId
import ir.vasl.samplechatkit.helper.TimeHelper.Companion.getCurrentTimestamp
import ir.vasl.samplechatkit.utils.PublicValues
import java.util.*

class MessageGeneratorHelper {

    companion object {

        private var isEnemy = true

        fun generateSampleMessageList(): ArrayList<ConversationModel> {
            val conversationModelList = ArrayList<ConversationModel>()
            for (i in 1..20) {
                when (i) {
                    2, 5 -> conversationModelList.add(generateSampleMessage())
                    3, 6, 18, 19 -> conversationModelList.add(generateSampleVoiceMessage())
                    else -> conversationModelList.add(generateSampleFileMessage())
                }
            }
            return conversationModelList
        }

        private fun generateSampleMessage(): ConversationModel {
            val conversationModel =
                ConversationModel(PublicValues.chatId, UUID.randomUUID().toString())
            conversationModel.id = getIncreasingId()
            conversationModel.title = PublicValues.sampleUsername
            conversationModel.message = getRandomMessageInputFa()
            conversationModel.time = getCurrentTimestamp().toString()
            conversationModel.conversationStatus = ConversationStatus.DELIVERED
            conversationModel.fileType = FileType.NONE

            when (isEnemy) {
                true -> conversationModel.conversationType = ConversationType.SERVER
                false -> conversationModel.conversationType = ConversationType.CLIENT
            }

            isEnemy = !isEnemy

            return conversationModel
        }

        private fun generateSampleTextMessage(): ConversationModel {
            val conversationModel =
                ConversationModel(PublicValues.chatId, UUID.randomUUID().toString())
            conversationModel.id = getIncreasingId()
            conversationModel.title = PublicValues.sampleUsername
            conversationModel.message = getRandomMessageInputFa()
            conversationModel.time = getCurrentTimestamp().toString()
            conversationModel.conversationStatus = ConversationStatus.DELIVERED
            conversationModel.fileType = FileType.NONE

            when (isEnemy) {
                true -> conversationModel.conversationType = ConversationType.SERVER
                false -> conversationModel.conversationType = ConversationType.CLIENT
            }

            isEnemy = !isEnemy

            return conversationModel
        }

        private fun generateSampleImageMessage(): ConversationModel {
            val conversationModel =
                ConversationModel(PublicValues.chatId, UUID.randomUUID().toString())
            conversationModel.id = getIncreasingId()
            conversationModel.title = PublicValues.sampleUsername
            conversationModel.message = getRandomMessageInputFa()
            conversationModel.time = getCurrentTimestamp().toString()
            conversationModel.conversationStatus = ConversationStatus.DELIVERED
            conversationModel.fileType = FileType.NONE

            when (isEnemy) {
                true -> conversationModel.conversationType = ConversationType.SERVER
                false -> conversationModel.conversationType = ConversationType.CLIENT
            }

            isEnemy = !isEnemy

            return conversationModel
        }

        private fun generateSampleVoiceMessage(): ConversationModel {
            val conversationModel =
                ConversationModel(PublicValues.chatId, UUID.randomUUID().toString())
            conversationModel.id = getIncreasingId()
            conversationModel.title = PublicValues.sampleUsername
            conversationModel.message = getRandomMessageInputFa()
            conversationModel.time = getCurrentTimestamp().toString()
            conversationModel.conversationStatus = ConversationStatus.DELIVERED
            conversationModel.fileType = FileType.AUDIO
            conversationModel.fileAddress = "https://www.kozco.com/tech/piano2.wav";

            when (isEnemy) {
                true -> conversationModel.conversationType = ConversationType.SERVER
                false -> conversationModel.conversationType = ConversationType.CLIENT
            }

            isEnemy = !isEnemy

            return conversationModel
        }

        private fun generateSampleFileMessage(): ConversationModel {
            val conversationModel =
                ConversationModel(PublicValues.chatId, UUID.randomUUID().toString())
            conversationModel.id = getIncreasingId()
            conversationModel.title = PublicValues.sampleFileTitle
            conversationModel.message = PublicValues.sampleFileName
            conversationModel.time = getCurrentTimestamp().toString()
            conversationModel.conversationStatus = ConversationStatus.DELIVERED
            conversationModel.fileType = FileType.DOCUMENT
            conversationModel.fileAddress =
                "https://file-examples-com.github.io/uploads/2017/10/file-sample_150kB.pdf"

            when (isEnemy) {
                true -> conversationModel.conversationType = ConversationType.SERVER
                false -> conversationModel.conversationType = ConversationType.CLIENT
            }

            isEnemy = !isEnemy

            return conversationModel
        }

        private fun getRandomMessageInputFa(): String {
            return "لورم ایپسوم متن ساختگی با تولید سادگی نامفهوم از صنعت چاپ و با استفاده از طراحان گرافیک است. "
        }

        private fun getRandomMessageInputEn(): String {
            return "Lorem ipsum is placeholder text commonly used in the graphic, print, and publishing industries for previewing layouts and visual mockups."
        }
    }
}