package cz.fjerabek.thr10controller.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import cz.fjerabek.thr.data.midi.PresetMessage
import cz.fjerabek.thr10controller.R
import cz.fjerabek.thr10controller.databinding.PresetListRowLayoutBinding
import timber.log.Timber

class PresetAdapter(
    private val context: Context,
    var data: MutableLiveData<MutableList<PresetMessage>>,
    private val snackbarViewAnchor: View,
    var onDragRequest: ((ViewHolder) -> Unit)? = null,
    var onItemSelectedListener: ((PresetMessage, Int) -> Unit)? = null,
    var onItemLongClick: ((PresetMessage, Int) -> Unit)? = null
) : RecyclerView.Adapter<PresetAdapter.ViewHolder>(), ItemMoveCallback.ItemTouchEventsCallback {
    private data class RemovedItem(val index: Int, val preset: PresetMessage)

    private var orderChanged = false
    private var removedItem: RemovedItem? = null
    var selectedItem: Int = -1
        set(value) {
            val oldItem = field
            field = value
            if (oldItem != -1)
                notifyItemChanged(oldItem)
            if(field != -1)
                notifyItemChanged(field)
        }

    private val inflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view: View =
            inflater.inflate(R.layout.preset_list_row_layout, parent, false)

        val binding: PresetListRowLayoutBinding = DataBindingUtil.bind(view)!!
        return ViewHolder(view, binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemBinding.name = data.value?.get(position)?.name ?: "Empty"
        holder.itemBinding.dragHandle.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    Timber.d("Touch down")
                    onDragRequest?.invoke(holder)
                }
            }
            return@setOnTouchListener false
        }

        holder.itemBinding.deleteButton.setOnClickListener {
            Snackbar.make(
                snackbarViewAnchor,
                context.getString(
                    R.string.preset_removed,
                    data.value?.get(position)?.name ?: "Empty",
                ), Snackbar.LENGTH_LONG
            ).setAction(R.string.undo) {
                removedItem?.let {
                    data.value?.add(it.index, it.preset)
                    data.value = data.value
                    notifyDataSetChanged()
                }
            }.show()

            removedItem = data.value?.removeAt(position)?.let { it1 -> RemovedItem(position, it1) }
            data.value = data.value
            notifyDataSetChanged()
        }

        holder.itemBinding.presetRow.setOnLongClickListener {
            data.value?.get(position)?.let { it1 -> onItemLongClick?.invoke(it1, position) }
            true
        }

        holder.itemBinding.presetRow.setOnClickListener {
            data.value?.get(position)?.let { it1 ->
                onItemSelectedListener?.invoke(it1, position)
            }
        }

        if (selectedItem == position) {
            holder.itemBinding.presetName.setTextColor(context.resources.getColor(R.color.colorAccent, context.applicationContext.theme))
            holder.itemBinding.presetRow.setBackgroundColor(context.resources.getColor(R.color.colorPrimaryDark, context.applicationContext.theme))
        } else {
            holder.itemBinding.presetName.setTextColor(context.resources.getColor(R.color.textOnPrimary, context.applicationContext.theme))
            holder.itemBinding.presetRow.setBackgroundColor(context.resources.getColor(R.color.colorPrimary, context.applicationContext.theme))
        }
    }

    override fun getItemCount(): Int = data.value?.size ?: 0

    data class ViewHolder(val itemView: View, val itemBinding: PresetListRowLayoutBinding) :
        RecyclerView.ViewHolder(itemView)

    override fun onRowMoved(oldPos: Int, newPos: Int): Boolean {
        data.value?.let {
            it.add(newPos, it.removeAt(oldPos))
        }
        notifyItemMoved(oldPos, newPos)
        orderChanged = true
        return true
    }

    override fun onRowSelected(viewHolder: ViewHolder) {
    }

    override fun onRowClear(viewHolder: ViewHolder) {
        if (orderChanged) {
            orderChanged = false
            data.value = data.value
        }
    }

}


class ItemMoveCallback(private val callbackReceiver: ItemTouchEventsCallback) :
    ItemTouchHelper.Callback() {
    override fun isLongPressDragEnabled() = false
    override fun isItemViewSwipeEnabled() = false

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ) = makeMovementFlags(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0)

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ) = callbackReceiver.onRowMoved(viewHolder.adapterPosition, target.adapterPosition)

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder is PresetAdapter.ViewHolder) {
                callbackReceiver.onRowSelected(viewHolder)
            }
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        if (viewHolder is PresetAdapter.ViewHolder) {
            callbackReceiver.onRowClear(viewHolder)
        }
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
    }

    interface ItemTouchEventsCallback {
        fun onRowMoved(oldPos: Int, newPos: Int): Boolean
        fun onRowSelected(viewHolder: PresetAdapter.ViewHolder)
        fun onRowClear(viewHolder: PresetAdapter.ViewHolder)
    }
}
