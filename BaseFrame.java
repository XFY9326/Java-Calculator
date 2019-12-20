import javax.swing.*;
import java.awt.*;

/**
 * 基础窗口
 */
abstract class BaseFrame extends JFrame {
    private final int winWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
    private final int winHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
    private final JPanel basePanel;

    BaseFrame() {
        basePanel = buildFrame(winWidth, winHeight);
        setContentPane(basePanel);
    }

    /**
     * 居中显示窗口
     *
     * @param width  窗口宽度
     * @param height 窗口高度
     */
    void setCenterFrameSize(int width, int height) {
        setBounds((winWidth - width) / 2, (winHeight - height) / 2, width, height);
    }

    /**
     * 展示窗口
     */
    public void showFrame() {
        if (basePanel != null) {
            initComponents(basePanel);
        }
        setVisible(true);
    }

    /**
     * 构建窗口
     * 展示前才会调用
     *
     * @param width  屏幕宽度
     * @param height 屏幕高度
     * @return 窗口基础JPanel
     */
    protected abstract JPanel buildFrame(int width, int height);

    /**
     * 初始化空间
     *
     * @param basePanel 窗口基础JPanel
     */
    protected abstract void initComponents(final JPanel basePanel);
}
