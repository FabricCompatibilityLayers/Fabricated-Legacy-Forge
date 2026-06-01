/*
 * The FML Forge Mod Loader suite. Copyright (C) 2012 cpw
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
package cpw.mods.fml.relauncher;

public class DummyDownloader implements IDownloadDisplay
{

    @Override
    public void resetProgress(int sizeGuess)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void setPokeThread(Thread currentThread)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateProgress(int fullLength)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean shouldStopIt()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void updateProgressString(String string, Object... data)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public Object makeDialog()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void makeHeadless()
    {
        // TODO Auto-generated method stub

    }

}
