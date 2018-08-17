package wellijohn.org.treerecyclerview.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.List;

import wellijohn.org.treerecyclerview.ex.StopMsgException;
import wellijohn.org.treerecyclerview.vo.Tree;

/**
 * @author: WelliJohn
 * @time: 2018/8/3-15:32
 * @email: wellijohn1991@gmail.com
 * @desc:
 */
public abstract class BaseTreeRVAdapter<M extends Tree> extends BaseRVAdapterV2<RecyclerView.ViewHolder, M> {

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        try {
            getLevel(position, mDatas, new PosBean());
        } catch (StopMsgException ex) {
            int type = Integer.parseInt(ex.getMessage());
            M tree = ex.getTree();
            onBindViewHolder(type, tree, holder);
        }
    }


    @Override
    public int getItemViewType(int position) {
        try {
            getLevel(position, mDatas, new PosBean());
        } catch (StopMsgException ex) {
            return Integer.parseInt(ex.getMessage());
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return getTotal(mDatas);
    }

    public abstract void onBindViewHolder(int type, M tree, RecyclerView.ViewHolder holder);

    private Integer getTotal(List<M> paramDatas) {
        int total = 0;
        for (int i = 0, size = paramDatas == null ? 0 : paramDatas.size(); i < size; i++) {
            M tree = paramDatas.get(i);
            if (tree.isExpand()) {
                total++;
                total = getTotal(tree.getChilds()) + total;
            } else {
                total++;
            }
        }
        return total;
    }


    private void getLevel(int position, List<M> paramDatas, PosBean posBean) throws StopMsgException {
        if (paramDatas == null) return;
        for (M tree : paramDatas) {
            if (tree.isExpand()) {
                posBean.setIndex(posBean.getIndex() + 1);
                if (position + 1 == posBean.getIndex())
                    throw new StopMsgException(String.valueOf(tree.getLevel())).setTree(tree);
                getLevel(position, tree.getChilds(), posBean);
            } else {
                posBean.setIndex(posBean.getIndex() + 1);
                if (position + 1 == posBean.getIndex())
                    throw new StopMsgException(String.valueOf(tree.getLevel())).setTree(tree);
            }
        }
    }


    private static class PosBean {
        int index = 0;

        int getIndex() {
            return index;
        }

        void setIndex(int index) {
            this.index = index;
        }
    }
}