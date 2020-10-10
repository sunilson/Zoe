package at.sunilson.zoe

import android.app.backup.BackupAgent
import android.app.backup.BackupDataInput
import android.app.backup.BackupDataOutput
import android.app.backup.FullBackupDataOutput
import android.os.ParcelFileDescriptor
import android.util.Log

class CustomBackupAgent : BackupAgent() {

    override fun onRestore(
        data: BackupDataInput?,
        appVersionCode: Int,
        newState: ParcelFileDescriptor?
    ) {
        Log.d("Linus", data.toString())
        Log.d("Linus", appVersionCode.toString())
        Log.d("Linus", newState.toString())
    }

    override fun onRestore(
        data: BackupDataInput?,
        appVersionCode: Long,
        newState: ParcelFileDescriptor?
    ) {
        super.onRestore(data, appVersionCode, newState)
        Log.d("Linus", data.toString())
        Log.d("Linus", appVersionCode.toString())
        Log.d("Linus", newState.toString())
    }


    override fun onBackup(
        oldState: ParcelFileDescriptor?,
        data: BackupDataOutput?,
        newState: ParcelFileDescriptor?
    ) {
        Log.d("Linus", oldState.toString())
        Log.d("Linus", data.toString())
        Log.d("Linus", newState.toString())
    }
    override fun onFullBackup(data: FullBackupDataOutput?) {
        super.onFullBackup(data)
        Log.d("Linus", data.toString())
        Log.d("Linus", "Backup finished")
    }

    override fun onQuotaExceeded(backupDataBytes: Long, quotaBytes: Long) {
        super.onQuotaExceeded(backupDataBytes, quotaBytes)
        Log.d("Linus", "Quota exceeded")
    }

    override fun onRestoreFinished() {
        super.onRestoreFinished()
        Log.d("Linus", "Restore finished")
    }
}